package com.orvix.model;

/**
 * A single actionable review finding, produced by static analysis or the AI reviewer.
 *
 * @param category       review category
 * @param severity       severity
 * @param confidence     confidence the finding is real (false-positive reduction)
 * @param file           repository-relative file path (may be null/empty for repo-wide notes)
 * @param line           1-based line number, or 0 when not line-specific
 * @param title          short headline
 * @param description    explanation / evidence
 * @param recommendation suggested remediation (advice only — Orvix never edits code)
 * @param source         origin of the finding, e.g. "AI" or "PMD"
 */
public record Finding(
        Category category,
        Severity severity,
        Confidence confidence,
        String file,
        int line,
        String title,
        String description,
        String recommendation,
        String source) {

    /** Convenience builder for static-analysis sources where confidence is HIGH and source is a tool. */
    public static Finding fromTool(String tool, Category category, Severity severity,
                                   String file, int line, String title, String description) {
        return new Finding(category, severity, Confidence.HIGH, file, line, title, description, "", tool);
    }

    public String location() {
        if (file == null || file.isBlank()) {
            return "(general)";
        }
        return line > 0 ? file + ":" + line : file;
    }
}
