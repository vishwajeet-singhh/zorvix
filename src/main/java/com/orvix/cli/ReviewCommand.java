package com.orvix.cli;

import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.Callable;

import org.springframework.stereotype.Component;

import com.orvix.ai.OllamaUnavailableException;
import com.orvix.model.ReviewResult;
import com.orvix.report.MarkdownReporter;
import com.orvix.report.TerminalReporter;
import com.orvix.review.NotARepositoryException;
import com.orvix.review.ReviewService;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/** {@code orvix review [--base <branch>]} — reviews the current branch against a base branch. */
@Component
@Command(name = "review", mixinStandardHelpOptions = true,
        description = "Review the current branch against a base branch.")
public class ReviewCommand implements Callable<Integer> {

    @Option(names = {"--base", "-b"}, description = "Base branch to compare against (e.g. main, dev). "
            + "Auto-detected when omitted.")
    private String base;

    private final ReviewService reviewService;
    private final TerminalReporter terminalReporter;
    private final MarkdownReporter markdownReporter;

    public ReviewCommand(ReviewService reviewService, TerminalReporter terminalReporter,
                         MarkdownReporter markdownReporter) {
        this.reviewService = reviewService;
        this.terminalReporter = terminalReporter;
        this.markdownReporter = markdownReporter;
    }

    @Override
    public Integer call() {
        try {
            ReviewResult result = reviewService.review(
                    Optional.ofNullable(base), msg -> System.err.println("• " + msg));

            terminalReporter.print(result, System.out);

            Path report = markdownReporter.write(result);
            System.out.println("Report written to " + report);
            return 0;
        } catch (NotARepositoryException | OllamaUnavailableException ex) {
            System.err.println("\n" + ex.getMessage());
            return 1;
        } catch (Exception ex) {
            System.err.println("\nReview failed: " + ex.getMessage());
            return 1;
        }
    }
}
