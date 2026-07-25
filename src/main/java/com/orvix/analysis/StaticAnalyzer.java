package com.orvix.analysis;

import java.nio.file.Path;
import java.util.List;

import com.orvix.model.ChangedFile;
import com.orvix.model.Finding;

/**
 * A pluggable static-analysis backend. Implementations run over the changed files and contribute
 * findings that are fed to both the AI reviewer and the report. New backends (Checkstyle,
 * SpotBugs, ...) can be added simply by registering another Spring bean implementing this.
 */
public interface StaticAnalyzer {

    /** Short tool name, used as the finding source and in reports (e.g. "PMD"). */
    String name();

    /** Whether this analyzer can run in the current environment. */
    boolean isAvailable();

    /**
     * Analyzes the changed files and returns findings. Implementations must never throw — degrade
     * to an empty list on error so a single analyzer never breaks a review.
     */
    List<Finding> analyze(Path workTree, List<ChangedFile> changedFiles);
}
