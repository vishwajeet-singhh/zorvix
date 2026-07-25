package com.orvix.ai;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orvix.model.Category;
import com.orvix.model.Confidence;
import com.orvix.model.Finding;
import com.orvix.model.Severity;

class JsonReviewParserTest {

    private final JsonReviewParser parser = new JsonReviewParser(new ObjectMapper());

    @Test
    void parsesWellFormedReview() {
        String json = """
                {
                  "summary": "Looks mostly fine.",
                  "findings": [
                    {
                      "category": "SECURITY",
                      "severity": "HIGH",
                      "confidence": "HIGH",
                      "file": "UserService.java",
                      "line": 42,
                      "title": "Hardcoded secret",
                      "description": "API key embedded in source.",
                      "recommendation": "Move to configuration."
                    }
                  ],
                  "architectureReview": "Layering respected.",
                  "securityReview": "One hardcoded secret.",
                  "designViolations": ["SRP in OrderService"],
                  "recommendations": ["Externalize secrets"],
                  "reviewScore": 7.5
                }
                """;

        ParsedReview review = parser.parse(json);

        assertThat(review.summary()).isEqualTo("Looks mostly fine.");
        assertThat(review.findings()).hasSize(1);
        Finding f = review.findings().get(0);
        assertThat(f.category()).isEqualTo(Category.SECURITY);
        assertThat(f.severity()).isEqualTo(Severity.HIGH);
        assertThat(f.confidence()).isEqualTo(Confidence.HIGH);
        assertThat(f.line()).isEqualTo(42);
        assertThat(f.source()).isEqualTo("AI");
        assertThat(review.designViolations()).containsExactly("SRP in OrderService");
        assertThat(review.modelScore()).isEqualTo(7.5);
    }

    @Test
    void salvagesJsonWrappedInText() {
        String content = "Here is the review:\n{\"summary\":\"ok\",\"findings\":[]}\nThanks!";

        ParsedReview review = parser.parse(content);

        assertThat(review.summary()).isEqualTo("ok");
        assertThat(review.findings()).isEmpty();
    }

    @Test
    void degradesGracefullyOnGarbage() {
        ParsedReview review = parser.parse("not json at all");

        assertThat(review.findings()).isEmpty();
        assertThat(review.summary()).isNotBlank();
    }
}
