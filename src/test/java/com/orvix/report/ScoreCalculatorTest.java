package com.orvix.report;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.orvix.model.Category;
import com.orvix.model.Confidence;
import com.orvix.model.Finding;
import com.orvix.model.Severity;

class ScoreCalculatorTest {

    private final ScoreCalculator calculator = new ScoreCalculator();

    @Test
    void perfectScoreWithNoFindings() {
        assertThat(calculator.score(List.of())).isEqualTo(10.0);
    }

    @Test
    void highSeverityHighConfidenceReducesScore() {
        Finding f = new Finding(Category.SECURITY, Severity.HIGH, Confidence.HIGH,
                "A.java", 1, "secret", "desc", "fix", "AI");

        // 10 - (1.5 * 1.0) = 8.5
        assertThat(calculator.score(List.of(f))).isEqualTo(8.5);
    }

    @Test
    void lowConfidenceLowersPenalty() {
        Finding high = new Finding(Category.SECURITY, Severity.HIGH, Confidence.LOW,
                "A.java", 1, "maybe", "desc", "fix", "AI");

        // 10 - (1.5 * 0.3) = 9.55 -> rounded 9.6 (1 decimal, half-up via Math.round)
        assertThat(calculator.score(List.of(high))).isEqualTo(9.6);
    }

    @Test
    void scoreNeverNegative() {
        Finding f = new Finding(Category.SECURITY, Severity.HIGH, Confidence.HIGH,
                "A.java", 1, "x", "d", "r", "AI");
        List<Finding> many = java.util.Collections.nCopies(50, f);

        assertThat(calculator.score(many)).isEqualTo(0.0);
    }
}
