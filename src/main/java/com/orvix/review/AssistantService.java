package com.orvix.review;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.eclipse.jgit.api.Git;
import org.springframework.stereotype.Service;

import com.orvix.ai.OllamaClient;
import com.orvix.ai.OllamaProvisioner;
import com.orvix.ai.PromptBuilder;
import com.orvix.context.ContextBuilder;
import com.orvix.context.ProjectIndex;
import com.orvix.context.ProjectIndexer;
import com.orvix.git.GitService;
import com.orvix.model.ChangeType;
import com.orvix.model.ChangedFile;
import com.orvix.model.RelatedSource;

/**
 * Backs the {@code ask} and {@code explain} commands: free-text Q&A grounded in the current
 * changes, and a structured explanation of a single source file.
 */
@Service
public class AssistantService {

    private final OllamaProvisioner provisioner;
    private final OllamaClient client;
    private final PromptBuilder promptBuilder;
    private final ReviewContextLoader loader;
    private final GitService gitService;
    private final ProjectIndexer projectIndexer;
    private final ContextBuilder contextBuilder;

    public AssistantService(OllamaProvisioner provisioner, OllamaClient client, PromptBuilder promptBuilder,
                            ReviewContextLoader loader, GitService gitService,
                            ProjectIndexer projectIndexer, ContextBuilder contextBuilder) {
        this.provisioner = provisioner;
        this.client = client;
        this.promptBuilder = promptBuilder;
        this.loader = loader;
        this.gitService = gitService;
        this.projectIndexer = projectIndexer;
        this.contextBuilder = contextBuilder;
    }

    public String ask(String question, Optional<String> baseOverride, Consumer<String> progress) {
        provisioner.ensureReady(progress);
        LoadedContext loaded = loader.load(baseOverride, false, progress);
        progress.accept("Asking the local model...");
        try {
            return client.chat(
                    promptBuilder.assistantSystemPrompt(),
                    promptBuilder.askUserPrompt(loaded.context(), question),
                    null);
        } catch (IOException ex) {
            throw new IllegalStateException("Ask failed: " + ex.getMessage(), ex);
        }
    }

    public String explain(String filePath, Consumer<String> progress) {
        provisioner.ensureReady(progress);

        Path workTree = resolveWorkTree();
        Path file = workTree.resolve(filePath);
        if (!Files.isRegularFile(file)) {
            // Also try interpreting the path relative to the current directory.
            file = Path.of(System.getProperty("user.dir")).resolve(filePath);
        }
        if (!Files.isRegularFile(file)) {
            throw new IllegalArgumentException("File not found: " + filePath);
        }

        String content;
        try {
            content = Files.readString(file, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new IllegalStateException("Could not read " + filePath + ": " + ex.getMessage(), ex);
        }

        String relativePath = workTree.relativize(file.toAbsolutePath().normalize()).toString();
        progress.accept("Indexing project for context...");
        ProjectIndex index = projectIndexer.index(workTree);

        List<RelatedSource> related = List.of();
        if (relativePath.endsWith(".java")) {
            ChangedFile synthetic = new ChangedFile(relativePath, "", ChangeType.MODIFIED, "", 0, 0);
            related = contextBuilder.relatedSources(List.of(synthetic), workTree, index);
        }

        progress.accept("Explaining with the local model...");
        try {
            return client.chat(
                    promptBuilder.assistantSystemPrompt(),
                    promptBuilder.explainUserPrompt(relativePath, content, related),
                    null);
        } catch (IOException ex) {
            throw new IllegalStateException("Explain failed: " + ex.getMessage(), ex);
        }
    }

    /** Work tree of the current repo, or the current directory when not in a repo. */
    private Path resolveWorkTree() {
        Optional<Git> git = gitService.openCurrent();
        if (git.isPresent()) {
            try (Git g = git.get()) {
                return gitService.workTree(g);
            }
        }
        return Path.of(System.getProperty("user.dir"));
    }
}
