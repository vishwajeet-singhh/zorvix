package com.orvix.ai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orvix.config.OrvixProperties;

/**
 * Thin client over the local Ollama HTTP API, using only the JDK HTTP client (no extra
 * dependency and no source ever leaving the machine). Supports reachability checks, model
 * listing/pulling and chat completion (optionally with structured-output JSON).
 */
@Component
public class OllamaClient {

    private static final Logger log = LoggerFactory.getLogger(OllamaClient.class);

    private final OrvixProperties properties;
    private final ObjectMapper mapper;
    private final HttpClient http;

    public OllamaClient(OrvixProperties properties, ObjectMapper mapper) {
        this.properties = properties;
        this.mapper = mapper;
        this.http = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    private String baseUrl() {
        return properties.getOllama().getBaseUrl().replaceAll("/+$", "");
    }

    /** True if the Ollama server answers {@code GET /api/version}. */
    public boolean isReachable() {
        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl() + "/api/version"))
                    .timeout(Duration.ofSeconds(3))
                    .GET()
                    .build();
            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (IOException | InterruptedException ex) {
            if (ex instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            return false;
        }
    }

    /** Server version string, or empty when unreachable. */
    public String version() {
        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl() + "/api/version"))
                    .timeout(Duration.ofSeconds(3))
                    .GET()
                    .build();
            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return mapper.readTree(response.body()).path("version").asText("");
            }
        } catch (IOException | InterruptedException ex) {
            if (ex instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
        }
        return "";
    }

    /** Names (with tags) of all locally installed models. */
    public Set<String> installedModels() {
        Set<String> names = new TreeSet<>();
        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl() + "/api/tags"))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();
            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonNode models = mapper.readTree(response.body()).path("models");
                models.forEach(m -> names.add(m.path("name").asText()));
            }
        } catch (IOException | InterruptedException ex) {
            if (ex instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
        }
        return names;
    }

    /** True if {@code model} (matching with or without an implicit ":latest" tag) is installed. */
    public boolean hasModel(String model) {
        Set<String> installed = installedModels();
        if (installed.contains(model)) {
            return true;
        }
        String normalized = model.contains(":") ? model : model + ":latest";
        return installed.contains(normalized);
    }

    /**
     * Pulls {@code model}, streaming progress lines to {@code progress}. Blocks until complete.
     *
     * @throws IOException on transport failure
     */
    public void pullModel(String model, Consumer<String> progress) throws IOException {
        Map<String, Object> body = Map.of("name", model, "stream", true);
        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl() + "/api/pull"))
                .timeout(Duration.ofHours(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
                .build();
        try {
            HttpResponse<InputStream> response =
                    http.send(request, HttpResponse.BodyHandlers.ofInputStream());
            if (response.statusCode() != 200) {
                throw new IOException("Ollama pull failed with HTTP " + response.statusCode());
            }
            String last = "";
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(response.body(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.isBlank()) {
                        continue;
                    }
                    JsonNode node = mapper.readTree(line);
                    String status = node.path("status").asText("");
                    if (!status.isEmpty() && !status.equals(last)) {
                        progress.accept(status);
                        last = status;
                    }
                    if (node.hasNonNull("error")) {
                        throw new IOException("Ollama pull error: " + node.get("error").asText());
                    }
                }
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IOException("Model pull interrupted", ex);
        }
    }

    /**
     * Sends a chat completion. When {@code format} is non-null it is passed as Ollama's
     * structured-output {@code format} (either the string "json" or a JSON-schema node), forcing
     * machine-readable output.
     *
     * @return the assistant message content
     */
    public String chat(String systemPrompt, String userPrompt, Object format) throws IOException {
        List<Map<String, String>> messages = new ArrayList<>();
        if (systemPrompt != null && !systemPrompt.isBlank()) {
            messages.add(Map.of("role", "system", "content", systemPrompt));
        }
        messages.add(Map.of("role", "user", "content", userPrompt));
        return chat(messages, format);
    }

    /**
     * Sends a full message list (system + alternating user/assistant turns). Keeping a constant
     * leading context lets Ollama reuse its KV cache across turns, so follow-up questions in an
     * interactive session evaluate the large context only once.
     *
     * @return the assistant message content
     */
    public String chat(List<Map<String, String>> messages, Object format) throws IOException {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("model", properties.getOllama().getModel());
        body.put("messages", messages);
        body.put("stream", false);
        body.put("keep_alive", properties.getOllama().getKeepAlive());
        // Deterministic, focused output.
        body.put("options", Map.of("temperature", 0.1, "num_ctx", properties.getOllama().getNumCtx()));
        if (format != null) {
            body.put("format", format);
        }

        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl() + "/api/chat"))
                .timeout(properties.getOllama().getRequestTimeout())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
                .build();
        try {
            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IOException("Ollama chat failed with HTTP " + response.statusCode()
                        + ": " + response.body());
            }
            JsonNode root = mapper.readTree(response.body());
            return root.path("message").path("content").asText("");
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IOException("Chat request interrupted", ex);
        }
    }
}
