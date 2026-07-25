package com.orvix.model;

import java.util.List;

/**
 * Everything (other than the prompt instructions) that is handed to the AI reviewer for a run:
 * repository metadata, the changed files with their diffs, the relevant surrounding source, a
 * compact project-structure summary, and any static-analysis findings gathered beforehand.
 *
 * @param repoName             repository directory name
 * @param branch               current branch
 * @param baseBranch           resolved base branch
 * @param changedFiles         files changed in {@code base...HEAD}
 * @param relatedSources       trimmed surrounding source, already budget-capped
 * @param projectStructure     compact summary of packages/layers
 * @param staticFindings       findings produced by static analysis (PMD, ...)
 */
public record CodeContext(
        String repoName,
        String branch,
        String baseBranch,
        List<ChangedFile> changedFiles,
        List<RelatedSource> relatedSources,
        String projectStructure,
        List<Finding> staticFindings) {

    public int filesChanged() {
        return changedFiles == null ? 0 : changedFiles.size();
    }
}
