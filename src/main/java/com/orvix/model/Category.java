package com.orvix.model;

/** Review category a finding belongs to. */
public enum Category {
    SECURITY("Security"),
    CODE_QUALITY("Code Quality"),
    RELIABILITY("Reliability"),
    PERFORMANCE("Performance"),
    SPRING_BOOT("Spring Boot"),
    REST_API("REST API"),
    DATABASE("Database"),
    CONCURRENCY("Concurrency"),
    ARCHITECTURE("Architecture"),
    DESIGN_PRINCIPLE("Design Principle"),
    OTHER("Other");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }

    /** Tolerant parse for model output; defaults to {@link #OTHER} when unrecognised. */
    public static Category from(String value) {
        if (value == null) {
            return OTHER;
        }
        String v = value.trim().toUpperCase().replace(' ', '_').replace('-', '_');
        return switch (v) {
            case "SECURITY" -> SECURITY;
            case "CODE_QUALITY", "QUALITY", "CODESTYLE", "STYLE" -> CODE_QUALITY;
            case "RELIABILITY", "BUG", "BUGS", "CORRECTNESS" -> RELIABILITY;
            case "PERFORMANCE", "PERF" -> PERFORMANCE;
            case "SPRING_BOOT", "SPRING" -> SPRING_BOOT;
            case "REST_API", "REST", "API" -> REST_API;
            case "DATABASE", "DB", "PERSISTENCE", "JPA" -> DATABASE;
            case "CONCURRENCY", "THREADING", "THREAD_SAFETY" -> CONCURRENCY;
            case "ARCHITECTURE", "ARCH", "DESIGN" -> ARCHITECTURE;
            case "DESIGN_PRINCIPLE", "SOLID", "DRY", "KISS", "YAGNI", "PRINCIPLE" -> DESIGN_PRINCIPLE;
            default -> OTHER;
        };
    }
}
