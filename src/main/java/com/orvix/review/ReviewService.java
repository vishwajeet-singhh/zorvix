package com.orvix.review;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;

import com.orvix.ai.JsonReviewParser;
import com.orvix.ai.OllamaClient;
import com.orvix.ai.OllamaProvisioner;
import com.orvix.ai.ParsedReview;
import com.orvix.ai.PromptBuilder;
import com.orvix.model.CodeContext;
import com.orvix.model.Finding;
import com.orvix.report.ScoreCalculator;

/**
 * Orchestrates a full review: ensure Ollama is ready, load the diff/context, run static analysis,
 * ask the model, merge findings and compute the score. Returns a {@link com.orvix.model.ReviewResult}.
 */
@Service
public class ReviewService {

    private final OllamaProvisioner provisioner;
    private final OllamaClient client;
    private final ReviewContextLoader loader;
    private final PromptBuilder promptBuilder;
    private final JsonReviewParser parser;
    private final ScoreCalculator scoreCalculator;

    public ReviewService(OllamaProvisioner provisioner, OllamaClient client, ReviewContextLoader loader,
                         PromptBuilder promptBuilder, JsonReviewParser parser, ScoreCalculator scoreCalculator) {
        this.provisioner = provisioner;
        this.client = client;
        this.loader = loader;
        this.promptBuilder = promptBuilder;
        this.parser = parser;
        this.scoreCalculator = scoreCalculator;
    }

    public com.orvix.model.ReviewResult review(Optional<String> baseOverride, Consumer<String> progress) {
        provisioner.ensureReady(progress);
        LoadedContext loaded = loader.load(baseOverride, true, progress);
        CodeContext ctx = loaded.context();

        if (ctx.changedFiles().isEmpty()) {
            return new com.orvix.model.ReviewResult(
                    ctx.repoName(), ctx.branch(), ctx.baseBranch(), 0,
                    "No changes detected between " + ctx.branch() + " and " + ctx.baseBranch() + ".",
                    List.of(), "", "", List.of(), List.of(), 10.0);
        }

        progress.accept("Running AI review with the local model...");
        ParsedReview parsed;
        try {
            String content = client.chat(
                    promptBuilder.systemPrompt(),
                    promptBuilder.reviewUserPrompt(ctx),
                    promptBuilder.reviewFormatSchema());
            parsed = parser.parse(content);
        } catch (IOException ex) {
            throw new IllegalStateException("AI review failed: " + ex.getMessage(), ex);
        }

        List<Finding> merged = mergeFindings(ctx.staticFindings(), parsed.findings());
        double score = scoreCalculator.score(merged);

        return new com.orvix.model.ReviewResult(
                ctx.repoName(), ctx.branch(), ctx.baseBranch(), ctx.filesChanged(),
                parsed.summary(), merged,
                parsed.architectureReview(), parsed.securityReview(),
                parsed.designViolations(), parsed.recommendations(), score);
    }

    /** Combines static + AI findings, drops exact duplicates, and orders by severity then confidence. */
    private List<Finding> mergeFindings(List<Finding> staticFindings, List<Finding> aiFindings) {
        List<Finding> all = new ArrayList<>();
        Set<String> seen = new LinkedHashSet<>();
        for (Finding f : concat(staticFindings, aiFindings)) {
            String key = f.location() + "|" + f.title().toLowerCase().trim();
            if (seen.add(key)) {
                all.add(f);
            }
        }
        all.sort(Comparator
                .comparing(Finding::severity)
                .thenComparing(Finding::confidence));
        return all;
    }

    private List<Finding> concat(List<Finding> a, List<Finding> b) {
        List<Finding> list = new ArrayList<>(a == null ? List.of() : a);
        if (b != null) {
            list.addAll(b);
        }
        return list;
    }
}
