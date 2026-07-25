package com.orvix.config;

import java.time.Duration;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Strongly-typed configuration for Orvix, bound from the {@code orvix.*} section of
 * application.yaml (and overridable via environment variables / CLI system properties).
 */
@ConfigurationProperties(prefix = "orvix")
public class OrvixProperties {

    private final Ollama ollama = new Ollama();
    private final Review review = new Review();

    public Ollama getOllama() {
        return ollama;
    }

    public Review getReview() {
        return review;
    }

    /** Ollama connection + provisioning settings. */
    public static class Ollama {
        private String baseUrl = "http://localhost:11434";
        private String model = "qwen2.5-coder:7b";
        private boolean autoStart = true;
        private boolean autoPull = true;
        private Duration startupTimeout = Duration.ofSeconds(120);
        private Duration requestTimeout = Duration.ofSeconds(300);
        private String dockerComposeService = "ollama";
        // How long Ollama keeps the model loaded between calls (avoids reload latency).
        private String keepAlive = "30m";
        // Model context window. Larger = more context but slower inference.
        private int numCtx = 8192;

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public boolean isAutoStart() {
            return autoStart;
        }

        public void setAutoStart(boolean autoStart) {
            this.autoStart = autoStart;
        }

        public boolean isAutoPull() {
            return autoPull;
        }

        public void setAutoPull(boolean autoPull) {
            this.autoPull = autoPull;
        }

        public Duration getStartupTimeout() {
            return startupTimeout;
        }

        public void setStartupTimeout(Duration startupTimeout) {
            this.startupTimeout = startupTimeout;
        }

        public Duration getRequestTimeout() {
            return requestTimeout;
        }

        public void setRequestTimeout(Duration requestTimeout) {
            this.requestTimeout = requestTimeout;
        }

        public String getDockerComposeService() {
            return dockerComposeService;
        }

        public void setDockerComposeService(String dockerComposeService) {
            this.dockerComposeService = dockerComposeService;
        }

        public String getKeepAlive() {
            return keepAlive;
        }

        public void setKeepAlive(String keepAlive) {
            this.keepAlive = keepAlive;
        }

        public int getNumCtx() {
            return numCtx;
        }

        public void setNumCtx(int numCtx) {
            this.numCtx = numCtx;
        }
    }

    /** Review behaviour: branch detection + context budgeting + output. */
    public static class Review {
        private List<String> baseBranchPriority = List.of(
                "origin/dev", "origin/develop", "origin/main", "origin/master",
                "dev", "develop", "main", "master");
        private int maxContextChars = 24_000;
        private int maxRelatedFiles = 12;
        private String reportDir = ".orvix";

        public List<String> getBaseBranchPriority() {
            return baseBranchPriority;
        }

        public void setBaseBranchPriority(List<String> baseBranchPriority) {
            this.baseBranchPriority = baseBranchPriority;
        }

        public int getMaxContextChars() {
            return maxContextChars;
        }

        public void setMaxContextChars(int maxContextChars) {
            this.maxContextChars = maxContextChars;
        }

        public int getMaxRelatedFiles() {
            return maxRelatedFiles;
        }

        public void setMaxRelatedFiles(int maxRelatedFiles) {
            this.maxRelatedFiles = maxRelatedFiles;
        }

        public String getReportDir() {
            return reportDir;
        }

        public void setReportDir(String reportDir) {
            this.reportDir = reportDir;
        }
    }
}
