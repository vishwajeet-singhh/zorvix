package com.orvix.ai;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.orvix.model.ChangedFile;
import com.orvix.model.CodeContext;
import com.orvix.model.Finding;
import com.orvix.model.RelatedSource;

/**
 * Assembles the prompts and the structured-output JSON schema sent to the model. Encodes the
 * "AI Prompt Contract": a principal-engineer review of only the modified implementation, biased
 * toward precision (no invented or speculative findings), with JSON-only output for {@code review}.
 */
@Component
public class PromptBuilder {

    private static final String SYSTEM_PROMPT = """
            You are a principal software engineer performing a senior-level pre-merge code review.
            You review ONLY the modified implementation shown in the diffs, using the surrounding
            project code purely as context.

            Evaluate: correctness, reliability (null-safety, exception handling, resource leaks),
            security (hardcoded secrets/credentials, injection, auth), performance (N+1 queries,
            needless work), concurrency (thread-safety, shared state), Spring Boot conventions
            (constructor injection, transaction boundaries, validation, bean design), REST API
            design and HTTP semantics, database/entity/query design, maintainability, and the
            principles SOLID, DRY, KISS, YAGNI, separation of concerns, composition over
            inheritance, high cohesion, low coupling, encapsulation, layering and dependency
            direction. Identify architectural drift introduced by these changes.

            Rules:
            - Prefer precision over volume. Report only actionable, evidence-based findings.
            - Do NOT invent issues or raise purely theoretical concerns.
            - Assign each finding a confidence of HIGH, MEDIUM, or LOW.
            - Never suggest that code be auto-generated or rewritten for the user; give advice only.
            - Output JSON only, conforming exactly to the requested schema. No prose outside JSON.
            """;

    private static final String ASSISTANT_SYSTEM_PROMPT = """
            You are a principal software engineer assisting a developer with their current code
            changes. Answer accurately and concretely, grounded only in the code provided. If the
            code does not contain enough information, say so rather than guessing. Respond in clear
            plain text (no JSON, no code rewrites — advice only).
            """;

    public String systemPrompt() {
        return SYSTEM_PROMPT;
    }

    public String assistantSystemPrompt() {
        return ASSISTANT_SYSTEM_PROMPT;
    }

    /**
     * Renders the repository context (metadata, structure, static findings, diffs, related code)
     * without any task instruction. Reused as a constant prefix by review and by the interactive
     * chat session so the context is only assembled — and KV-cached by Ollama — once.
     */
    public String contextBlock(CodeContext ctx) {
        StringBuilder sb = new StringBuilder();
        sb.append("# Repository\n")
                .append("name: ").append(ctx.repoName()).append('\n')
                .append("branch: ").append(ctx.branch()).append('\n')
                .append("base: ").append(ctx.baseBranch()).append('\n')
                .append("files changed: ").append(ctx.filesChanged()).append("\n\n");

        sb.append("# Project structure (summary)\n")
                .append(ctx.projectStructure()).append('\n');

        appendStaticFindings(sb, ctx.staticFindings());
        appendChangedFiles(sb, ctx.changedFiles());
        appendRelatedSources(sb, ctx.relatedSources());
        return sb.toString();
    }

    /** Builds the user prompt for a full review from the assembled context. */
    public String reviewUserPrompt(CodeContext ctx) {
        return contextBlock(ctx)
                + "\n# Task\n"
                + "Review the changes above and return findings as JSON per the schema. "
                + "Use file paths and line numbers from the diffs. "
                + "Incorporate or refine the static-analysis findings where relevant, but do "
                + "not duplicate them verbatim.\n";
    }

    /** System prompt for the interactive chat: assistant role plus the one-time context prefix. */
    public String chatSystemPrompt(CodeContext ctx) {
        return assistantSystemPrompt()
                + "\nThe developer's current changes and relevant surrounding code follow. "
                + "Answer their questions grounded in this context.\n\n"
                + contextBlock(ctx);
    }

    public String askUserPrompt(CodeContext ctx, String question) {
        StringBuilder sb = new StringBuilder();
        sb.append("Question about the current changes:\n")
                .append(question).append("\n\n");
        appendChangedFiles(sb, ctx.changedFiles());
        appendRelatedSources(sb, ctx.relatedSources());
        sb.append("\nAnswer concisely and concretely, citing files/lines where relevant. ")
                .append("Plain text (no JSON).");
        return sb.toString();
    }

    public String explainUserPrompt(String path, String fileContent, List<RelatedSource> related) {
        StringBuilder sb = new StringBuilder();
        sb.append("Explain the following Java source file.\n\n")
                .append("## File: ").append(path).append('\n')
                .append("```java\n").append(fileContent).append("\n```\n");
        appendRelatedSources(sb, related);
        sb.append("\nDescribe, as a senior engineer, in clear sections:\n")
                .append("1. Responsibility\n2. Dependencies\n3. Risks\n4. Design concerns\n")
                .append("5. Refactoring opportunities\n6. Architecture observations\n")
                .append("Plain text (no JSON).");
        return sb.toString();
    }

    private void appendStaticFindings(StringBuilder sb, List<Finding> findings) {
        if (findings == null || findings.isEmpty()) {
            return;
        }
        sb.append("\n# Static-analysis findings (from tools)\n");
        for (Finding f : findings) {
            sb.append("- [").append(f.source()).append("] ")
                    .append(f.severity()).append(' ')
                    .append(f.location()).append(" — ")
                    .append(f.title()).append(": ").append(f.description()).append('\n');
        }
    }

    private void appendChangedFiles(StringBuilder sb, List<ChangedFile> changedFiles) {
        sb.append("\n# Changed files (diffs)\n");
        for (ChangedFile cf : changedFiles) {
            sb.append("\n## ").append(cf.changeType()).append(' ').append(cf.path()).append('\n');
            if (cf.patch() != null && !cf.patch().isBlank()) {
                sb.append("```diff\n").append(cf.patch()).append("\n```\n");
            } else {
                sb.append("(no textual diff available)\n");
            }
        }
    }

    private void appendRelatedSources(StringBuilder sb, List<RelatedSource> related) {
        if (related == null || related.isEmpty()) {
            return;
        }
        sb.append("\n# Relevant surrounding source (context only — do not review)\n");
        for (RelatedSource rs : related) {
            sb.append("\n## ").append(rs.path()).append("  (").append(rs.relation()).append(")\n")
                    .append("```java\n").append(rs.content()).append("\n```\n");
        }
    }

    /**
     * JSON schema for Ollama structured output, ensuring the model returns a parseable review.
     */
    public Map<String, Object> reviewFormatSchema() {
        Map<String, Object> finding = new LinkedHashMap<>();
        finding.put("type", "object");
        finding.put("properties", ordered(
                "category", stringType(),
                "severity", enumType("HIGH", "MEDIUM", "LOW"),
                "confidence", enumType("HIGH", "MEDIUM", "LOW"),
                "file", stringType(),
                "line", Map.of("type", "integer"),
                "title", stringType(),
                "description", stringType(),
                "recommendation", stringType()));
        finding.put("required", List.of("category", "severity", "confidence", "title", "description"));

        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("type", "object");
        schema.put("properties", ordered(
                "summary", stringType(),
                "findings", arrayOf(finding),
                "architectureReview", stringType(),
                "securityReview", stringType(),
                "designViolations", arrayOf(stringType()),
                "recommendations", arrayOf(stringType()),
                "reviewScore", Map.of("type", "number")));
        schema.put("required", List.of(
                "summary", "findings", "architectureReview", "securityReview",
                "designViolations", "recommendations"));
        return schema;
    }

    private Map<String, Object> stringType() {
        return Map.of("type", "string");
    }

    private Map<String, Object> enumType(String... values) {
        return Map.of("type", "string", "enum", List.of(values));
    }

    private Map<String, Object> arrayOf(Object itemSchema) {
        Map<String, Object> arr = new LinkedHashMap<>();
        arr.put("type", "array");
        arr.put("items", itemSchema);
        return arr;
    }

    private Map<String, Object> ordered(Object... kv) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < kv.length; i += 2) {
            map.put((String) kv[i], kv[i + 1]);
        }
        return map;
    }
}
