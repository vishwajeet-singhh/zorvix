package com.orvix.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;

import org.springframework.stereotype.Component;

import com.orvix.ai.OllamaClient;
import com.orvix.ai.OllamaProvisioner;
import com.orvix.ai.OllamaUnavailableException;
import com.orvix.ai.PromptBuilder;
import com.orvix.model.CodeContext;
import com.orvix.review.LoadedContext;
import com.orvix.review.NotARepositoryException;
import com.orvix.review.ReviewContextLoader;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * {@code orvix chat} — an interactive session that loads the diff/context <em>once</em>, keeps the
 * model warm, and answers follow-up questions quickly. The context is sent as a constant system
 * prefix so Ollama reuses its KV cache across turns.
 */
@Component
@Command(name = "chat", mixinStandardHelpOptions = true,
        description = "Interactive Q&A about the current changes (context loaded once).")
public class ChatCommand implements Callable<Integer> {

    private static final Set<String> EXIT_WORDS = Set.of("exit", "quit", ":q", "\\q");

    @Option(names = {"--base", "-b"}, description = "Base branch to compare against. Auto-detected when omitted.")
    private String base;

    private final OllamaProvisioner provisioner;
    private final ReviewContextLoader loader;
    private final PromptBuilder promptBuilder;
    private final OllamaClient client;

    public ChatCommand(OllamaProvisioner provisioner, ReviewContextLoader loader,
                       PromptBuilder promptBuilder, OllamaClient client) {
        this.provisioner = provisioner;
        this.loader = loader;
        this.promptBuilder = promptBuilder;
        this.client = client;
    }

    @Override
    public Integer call() {
        try {
            provisioner.ensureReady(msg -> System.err.println("• " + msg));
            LoadedContext loaded = loader.load(Optional.ofNullable(base), false,
                    msg -> System.err.println("• " + msg));
            CodeContext ctx = loaded.context();

            String system = promptBuilder.chatSystemPrompt(ctx);
            Map<String, String> systemMessage = message("system", system);

            System.err.println("• Preparing context (one-time)...");
            warm(systemMessage);

            return runRepl(ctx, systemMessage);
        } catch (NotARepositoryException | OllamaUnavailableException ex) {
            System.err.println("\n" + ex.getMessage());
            return 1;
        } catch (Exception ex) {
            System.err.println("\nChat failed: " + ex.getMessage());
            return 1;
        }
    }

    private int runRepl(CodeContext ctx, Map<String, String> systemMessage) throws IOException {
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(systemMessage);

        System.out.println();
        System.out.println("Orvix chat — " + ctx.repoName() + " (" + ctx.branch()
                + " vs " + ctx.baseBranch() + "), " + ctx.filesChanged() + " file(s) changed.");
        System.out.println("Ask questions about your changes. Type 'exit' to quit.");
        System.out.println();

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        while (true) {
            System.out.print("orvix> ");
            System.out.flush();
            String line = in.readLine();
            if (line == null) {
                break; // EOF (Ctrl-D)
            }
            String question = line.trim();
            if (question.isEmpty()) {
                continue;
            }
            if (EXIT_WORDS.contains(question.toLowerCase())) {
                break;
            }

            messages.add(message("user", question));
            try {
                String answer = client.chat(messages, null).trim();
                System.out.println();
                System.out.println(answer);
                System.out.println();
                messages.add(message("assistant", answer));
            } catch (IOException ex) {
                System.err.println("(request failed: " + ex.getMessage() + ")");
                messages.remove(messages.size() - 1); // drop the unanswered question
            }
        }
        System.out.println("Bye.");
        return 0;
    }

    /** Primes the model: loads weights and evaluates the (constant) context prefix once. */
    private void warm(Map<String, String> systemMessage) {
        try {
            client.chat(List.of(systemMessage, message("user", "Reply with: ready")), null);
        } catch (IOException ex) {
            // Non-fatal; the first real question will simply pay the cost instead.
            System.err.println("• (warm-up skipped: " + ex.getMessage() + ")");
        }
    }

    private Map<String, String> message(String role, String content) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("role", role);
        map.put("content", content);
        return map;
    }
}
