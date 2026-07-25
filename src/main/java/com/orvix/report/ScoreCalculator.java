package com.orvix.report;

import java.util.List;

import org.springframework.stereotype.Component;

import com.orvix.model.Confidence;
import com.orvix.model.Finding;
import com.orvix.model.Severity;

/**
 * Computes a deterministic 0–10 review score from the findings, weighting by severity and
 * confidence. Computed locally rather than trusting the model's self-reported score, so the
 * number is stable and explainable.
 */
@Component
public class ScoreCalculator {

    public double score(List<Finding> findings) {
        if (findings == null || findings.isEmpty()) {
            return 10.0;
        }
        double penalty = 0.0;
        for (Finding f : findings) {
            penalty += severityWeight(f.severity()) * confidenceWeight(f.confidence());
        }
        double score = 10.0 - penalty;
        score = Math.max(0.0, Math.min(10.0, score));
        return Math.round(score * 10.0) / 10.0;
    }

    private double severityWeight(Severity severity) {
        return switch (severity) {
            case HIGH -> 1.5;
            case MEDIUM -> 0.6;
            case LOW -> 0.2;
        };
    }

    private double confidenceWeight(Confidence confidence) {
        return switch (confidence) {
            case HIGH -> 1.0;
            case MEDIUM -> 0.6;
            case LOW -> 0.3;
        };
    }
}
