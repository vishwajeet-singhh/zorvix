package com.orvix.model;

/** Confidence that a finding is real and actionable (false-positive reduction). */
public enum Confidence {
    HIGH,
    MEDIUM,
    LOW;

    /** Tolerant parse for model output; defaults to {@link #MEDIUM} when unrecognised. */
    public static Confidence from(String value) {
        if (value == null) {
            return MEDIUM;
        }
        return switch (value.trim().toUpperCase()) {
            case "HIGH", "CERTAIN" -> HIGH;
            case "LOW", "SPECULATIVE", "POSSIBLE" -> LOW;
            default -> MEDIUM;
        };
    }
}
