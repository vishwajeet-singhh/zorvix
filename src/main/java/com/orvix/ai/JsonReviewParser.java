package com.orvix.ai;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orvix.model.Category;
import com.orvix.model.Confidence;
import com.orvix.model.Finding;
import com.orvix.model.Severity;

/**
 * Parses the model's JSON review response into a {@link ParsedReview}. Tolerant of minor
 * deviations: it extracts the JSON object even if the model wraps it in stray text.
 */
@Component
public class JsonReviewParser {

    private static final Logger log = LoggerFactory.getLogger(JsonReviewParser.class);

    private final ObjectMapper mapper;

    public JsonReviewParser(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public ParsedReview parse(String content) {
        JsonNode root = readJson(content);
        if (root == null || !root.isObject()) {
            return new ParsedReview(
                    "The model did not return a parseable review.",
                    List.of(), "", "", List.of(), List.of(), 0.0);
        }

        List<Finding> findings = new ArrayList<>();
        for (JsonNode node : root.path("findings")) {
            findings.add(toFinding(node));
        }

        return new ParsedReview(
                root.path("summary").asText(""),
                findings,
                root.path("architectureReview").asText(""),
                root.path("securityReview").asText(""),
                textList(root.path("designViolations")),
                textList(root.path("recommendations")),
                root.path("reviewScore").asDouble(0.0));
    }

    private Finding toFinding(JsonNode node) {
        Category category = Category.from(node.path("category").asText(""));
        Severity severity = Severity.from(node.path("severity").asText(""));
        Confidence confidence = Confidence.from(node.path("confidence").asText(""));
        String file = node.path("file").asText("");
        int line = node.path("line").asInt(0);
        String title = node.path("title").asText("");
        String description = node.path("description").asText("");
        String recommendation = node.path("recommendation").asText("");
        return new Finding(category, severity, confidence, file, line, title, description,
                recommendation, "AI");
    }

    private List<String> textList(JsonNode node) {
        if (node == null || !node.isArray()) {
            return List.of();
        }
        List<String> values = new ArrayList<>();
        node.forEach(n -> {
            String text = n.asText("");
            if (!text.isBlank()) {
                values.add(text);
            }
        });
        return values;
    }

    private JsonNode readJson(String content) {
        if (content == null || content.isBlank()) {
            return null;
        }
        try {
            return mapper.readTree(content);
        } catch (Exception ex) {
            // Fallback: salvage the outermost JSON object from surrounding text.
            int start = content.indexOf('{');
            int end = content.lastIndexOf('}');
            if (start >= 0 && end > start) {
                try {
                    return mapper.readTree(content.substring(start, end + 1));
                } catch (Exception ignored) {
                    log.warn("Unable to parse model JSON response");
                }
            }
            return null;
        }
    }
}
