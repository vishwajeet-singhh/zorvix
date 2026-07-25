package com.orvix.report;

import java.io.PrintStream;
import java.util.List;

import org.springframework.stereotype.Component;

import com.orvix.model.Finding;
import com.orvix.model.ReviewResult;
import com.orvix.model.Severity;

/**
 * Renders a review to the terminal in the compact Orvix layout: header, counts, findings grouped
 * by severity, and the overall score.
 */
@Component
public class TerminalReporter {

    public void print(ReviewResult result, PrintStream out) {
        out.println();
        out.println("ORVIX REVIEW");
        out.println();
        out.println("Repository: " + result.repoName());
        out.println("Branch:     " + result.branch());
        out.println("Base:       " + result.baseBranch());
        out.println();
        out.println("Files Changed: " + result.filesChanged());
        out.println("Issues Found:  " + result.issuesFound());

        if (result.summary() != null && !result.summary().isBlank()) {
            out.println();
            out.println(result.summary().trim());
        }

        printSeverityGroup(out, result.findings(), Severity.HIGH);
        printSeverityGroup(out, result.findings(), Severity.MEDIUM);
        printSeverityGroup(out, result.findings(), Severity.LOW);

        out.println();
        out.printf("Review Score: %.1f/10%n", result.reviewScore());
        out.println();
    }

    private void printSeverityGroup(PrintStream out, List<Finding> findings, Severity severity) {
        List<Finding> group = findings.stream().filter(f -> f.severity() == severity).toList();
        if (group.isEmpty()) {
            return;
        }
        out.println();
        out.println(severity.name());
        for (Finding f : group) {
            out.println("  " + f.location() + "  [" + f.category().displayName()
                    + ", confidence " + f.confidence() + "]");
            out.println("    " + f.title());
            if (f.description() != null && !f.description().isBlank()) {
                out.println("    " + oneLine(f.description()));
            }
            if (f.recommendation() != null && !f.recommendation().isBlank()) {
                out.println("    → " + oneLine(f.recommendation()));
            }
        }
    }

    private String oneLine(String text) {
        return text.replaceAll("\\s+", " ").trim();
    }
}
