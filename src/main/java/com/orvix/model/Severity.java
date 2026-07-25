package com.orvix.model;

/** Severity of a finding, ordered most-to-least severe. */
public enum Severity {
    HIGH,
    MEDIUM,
    LOW;

    /** Tolerant parse for model/tool output; defaults to {@link #MEDIUM} when unrecognised. */
    public static Severity from(String value) {
        if (value == null) {
            return MEDIUM;
        }
        return switch (value.trim().toUpperCase()) {
            case "HIGH", "CRITICAL", "BLOCKER", "ERROR" -> HIGH;
            case "LOW", "INFO", "MINOR", "TRIVIAL" -> LOW;
            default -> MEDIUM;
        };
    }
}
