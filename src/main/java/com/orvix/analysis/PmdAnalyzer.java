package com.orvix.analysis;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.orvix.model.Category;
import com.orvix.model.ChangeType;
import com.orvix.model.ChangedFile;
import com.orvix.model.Finding;
import com.orvix.model.Severity;

import net.sourceforge.pmd.PMDConfiguration;
import net.sourceforge.pmd.PmdAnalysis;
import net.sourceforge.pmd.reporting.Report;
import net.sourceforge.pmd.reporting.RuleViolation;

/**
 * Runs PMD over the changed Java source files using PMD's programmatic API. PMD analyses source
 * directly (no compilation required), making it ideal for pre-PR review.
 */
@Component
public class PmdAnalyzer implements StaticAnalyzer {

    private static final Logger log = LoggerFactory.getLogger(PmdAnalyzer.class);

    private static final List<String> RULESETS = List.of(
            "category/java/errorprone.xml",
            "category/java/bestpractices.xml",
            "category/java/design.xml",
            "category/java/multithreading.xml",
            "category/java/performance.xml");

    @Override
    public String name() {
        return "PMD";
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public List<Finding> analyze(Path workTree, List<ChangedFile> changedFiles) {
        List<Path> targets = new ArrayList<>();
        for (ChangedFile cf : changedFiles) {
            if (cf.isJava() && cf.changeType() != ChangeType.DELETED) {
                Path file = workTree.resolve(cf.path());
                if (Files.isRegularFile(file)) {
                    targets.add(file);
                }
            }
        }
        if (targets.isEmpty()) {
            return List.of();
        }

        try {
            return runPmd(workTree, targets);
        } catch (RuntimeException | LinkageError ex) {
            log.warn("PMD analysis skipped: {}", ex.getMessage());
            return List.of();
        }
    }

    private List<Finding> runPmd(Path workTree, List<Path> targets) {
        PMDConfiguration config = new PMDConfiguration();
        RULESETS.forEach(config::addRuleSet);
        targets.forEach(config::addInputPath);

        List<Finding> findings = new ArrayList<>();
        try (PmdAnalysis pmd = PmdAnalysis.create(config)) {
            Report report = pmd.performAnalysisAndCollectReport();
            for (RuleViolation v : report.getViolations()) {
                findings.add(toFinding(workTree, v));
            }
        }
        return findings;
    }

    private Finding toFinding(Path workTree, RuleViolation v) {
        String file = relativise(workTree, v.getFileId().getOriginalPath());
        int line = v.getBeginLine();
        String ruleName = v.getRule().getName();
        String description = v.getDescription();
        Severity severity = severityOf(v.getRule().getPriority().getPriority());
        Category category = categoryOf(ruleName, description);
        return Finding.fromTool("PMD", category, severity, file, line, ruleName, description);
    }

    private String relativise(Path workTree, String absolute) {
        if (absolute == null) {
            return "";
        }
        try {
            Path p = Path.of(absolute);
            return p.startsWith(workTree) ? workTree.relativize(p).toString() : absolute;
        } catch (RuntimeException ex) {
            return absolute;
        }
    }

    private Severity severityOf(int pmdPriority) {
        // PMD priority: 1 (highest) .. 5 (lowest).
        return switch (pmdPriority) {
            case 1, 2 -> Severity.HIGH;
            case 3 -> Severity.MEDIUM;
            default -> Severity.LOW;
        };
    }

    private Category categoryOf(String ruleName, String description) {
        String haystack = (ruleName + " " + description).toLowerCase();
        if (haystack.contains("thread") || haystack.contains("synchron") || haystack.contains("concurren")) {
            return Category.CONCURRENCY;
        }
        if (haystack.contains("performance") || haystack.contains("inefficient") || haystack.contains("loop")) {
            return Category.PERFORMANCE;
        }
        if (haystack.contains("null") || haystack.contains("exception") || haystack.contains("resource")
                || haystack.contains("close")) {
            return Category.RELIABILITY;
        }
        if (haystack.contains("coupling") || haystack.contains("cohesion") || haystack.contains("law of demeter")
                || haystack.contains("complexity") || haystack.contains("god")) {
            return Category.ARCHITECTURE;
        }
        return Category.CODE_QUALITY;
    }
}
