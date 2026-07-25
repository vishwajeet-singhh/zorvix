package com.orvix.report;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.stereotype.Component;

import com.orvix.config.OrvixProperties;
import com.orvix.model.Finding;
import com.orvix.model.ReviewResult;
import com.orvix.model.Severity;

/**
 * Writes the Markdown report to {@code <reportDir>/report.md}. This is the <em>only</em> file
 * Orvix writes — consistent with its read-only, advisor-not-editor contract.
 */
@Component
public class MarkdownReporter {

    private final OrvixProperties properties;

    public MarkdownReporter(OrvixProperties properties) {
        this.properties = properties;
    }

    /** Renders and writes the report, returning the path written. */
    public Path write(ReviewResult result) throws IOException {
        Path dir = Path.of(System.getProperty("user.dir")).resolve(properties.getReview().getReportDir());
        Files.createDirectories(dir);
        Path file = dir.resolve("report.md");
        Files.writeString(file, render(result), StandardCharsets.UTF_8);
        return file;
    }

    public String render(ReviewResult r) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Orvix Review Report\n\n");
        sb.append("- **Repository:** ").append(r.repoName()).append('\n');
        sb.append("- **Branch:** ").append(r.branch()).append('\n');
        sb.append("- **Base:** ").append(r.baseBranch()).append('\n');
        sb.append("- **Files changed:** ").append(r.filesChanged()).append('\n');
        sb.append("- **Issues found:** ").append(r.issuesFound())
                .append(" (HIGH ").append(r.countBySeverity(Severity.HIGH))
                .append(", MEDIUM ").append(r.countBySeverity(Severity.MEDIUM))
                .append(", LOW ").append(r.countBySeverity(Severity.LOW)).append(")\n");
        sb.append("- **Review score:** ").append(String.format("%.1f/10", r.reviewScore())).append("\n\n");

        section(sb, "Summary", r.summary());

        sb.append("## Findings\n\n");
        if (r.findings().isEmpty()) {
            sb.append("_No findings._\n\n");
        } else {
            appendFindings(sb, r.findings(), Severity.HIGH);
            appendFindings(sb, r.findings(), Severity.MEDIUM);
            appendFindings(sb, r.findings(), Severity.LOW);
        }

        section(sb, "Architecture Review", r.architectureReview());
        section(sb, "Security Review", r.securityReview());
        bulletSection(sb, "Design Principle Violations", r.designViolations());
        bulletSection(sb, "Recommendations", r.recommendations());

        sb.append("---\n\n");
        sb.append("_Generated locally by Orvix Developed By Vishwajeet Pratap Singh. No source code left this machine._\n");
        return sb.toString();
    }

    private void appendFindings(StringBuilder sb, List<Finding> findings, Severity severity) {
        List<Finding> group = findings.stream().filter(f -> f.severity() == severity).toList();
        if (group.isEmpty()) {
            return;
        }
        sb.append("### ").append(severity.name()).append("\n\n");
        for (Finding f : group) {
            sb.append("- **").append(escape(f.title())).append("** — `").append(f.location()).append("`  \n");
            sb.append("  _").append(f.category().displayName())
                    .append(" · confidence ").append(f.confidence())
                    .append(" · source ").append(f.source()).append("_  \n");
            if (notBlank(f.description())) {
                sb.append("  ").append(escape(f.description().trim())).append("  \n");
            }
            if (notBlank(f.recommendation())) {
                sb.append("  → ").append(escape(f.recommendation().trim())).append("\n");
            }
            sb.append('\n');
        }
    }

    private void section(StringBuilder sb, String title, String body) {
        sb.append("## ").append(title).append("\n\n");
        sb.append(notBlank(body) ? body.trim() : "_Not provided._").append("\n\n");
    }

    private void bulletSection(StringBuilder sb, String title, List<String> items) {
        sb.append("## ").append(title).append("\n\n");
        if (items == null || items.isEmpty()) {
            sb.append("_None._\n\n");
            return;
        }
        for (String item : items) {
            sb.append("- ").append(item.trim()).append('\n');
        }
        sb.append('\n');
    }

    private boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("\r", " ").replace("\n", " ");
    }
}
