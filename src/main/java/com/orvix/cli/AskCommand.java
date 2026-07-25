package com.orvix.cli;

import java.util.Optional;
import java.util.concurrent.Callable;

import org.springframework.stereotype.Component;

import com.orvix.ai.OllamaUnavailableException;
import com.orvix.review.AssistantService;
import com.orvix.review.NotARepositoryException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/** {@code orvix ask "<question>"} — answers a question grounded in the current changes. */
@Component
@Command(name = "ask", mixinStandardHelpOptions = true,
        description = "Ask a question about the current changes.")
public class AskCommand implements Callable<Integer> {

    @Parameters(index = "0", arity = "1", description = "The question, e.g. \"Is this thread safe?\"")
    private String question;

    @Option(names = {"--base", "-b"}, description = "Base branch to compare against. Auto-detected when omitted.")
    private String base;

    private final AssistantService assistantService;

    public AskCommand(AssistantService assistantService) {
        this.assistantService = assistantService;
    }

    @Override
    public Integer call() {
        try {
            String answer = assistantService.ask(
                    question, Optional.ofNullable(base), msg -> System.err.println("• " + msg));
            System.out.println();
            System.out.println(answer.trim());
            return 0;
        } catch (NotARepositoryException | OllamaUnavailableException ex) {
            System.err.println("\n" + ex.getMessage());
            return 1;
        } catch (Exception ex) {
            System.err.println("\nAsk failed: " + ex.getMessage());
            return 1;
        }
    }
}
