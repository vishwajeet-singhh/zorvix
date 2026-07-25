package com.orvix.model;

import java.util.List;

/**
 * The complete outcome of a review: merged findings plus the AI's narrative sections and an
 * overall score. Rendered to the terminal and to {@code .orvix/report.md}.
 *
 * @param repoName          repository name
 * @param branch            current branch
 * @param baseBranch        base branch reviewed against
 * @param filesChanged      number of changed files
 * @param summary           one-paragraph overview
 * @param findings          merged, deduped findings (AI + static), severity-ordered
 * @param architectureReview narrative architecture/design assessment
 * @param securityReview    narrative security assessment
 * @param designViolations  list of design-principle violations (SOLID/DRY/KISS/...)
 * @param recommendations   prioritised recommendations
 * @param reviewScore       overall score out of 10
 */
public record ReviewResult(
        String repoName,
        String branch,
        String baseBranch,
        int filesChanged,
        String summary,
        List<Finding> findings,
        String architectureReview,
        String securityReview,
        List<String> designViolations,
        List<String> recommendations,
        double reviewScore) {

    public long countBySeverity(Severity severity) {
        return findings == null ? 0 : findings.stream().filter(f -> f.severity() == severity).count();
    }

    public int issuesFound() {
        return findings == null ? 0 : findings.size();
    }
}
