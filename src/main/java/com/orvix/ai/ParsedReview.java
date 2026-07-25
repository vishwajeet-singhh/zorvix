package com.orvix.ai;

import java.util.List;

import com.orvix.model.Finding;

/**
 * The model's review parsed out of its JSON response, before repository metadata and the final
 * deterministic score are attached by the orchestrator.
 */
public record ParsedReview(
        String summary,
        List<Finding> findings,
        String architectureReview,
        String securityReview,
        List<String> designViolations,
        List<String> recommendations,
        double modelScore) {
}
