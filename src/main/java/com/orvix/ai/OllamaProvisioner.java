package com.orvix.ai;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.orvix.config.OrvixProperties;

/**
 * Makes Orvix usable on a machine that has <em>neither</em> Ollama running nor the model pulled.
 *
 * <p>Order of operations on {@link #ensureReady(Consumer)}:
 * <ol>
 *   <li>If Ollama already answers, use it.</li>
 *   <li>Otherwise, if {@code auto-start} is enabled and Docker is available, start an Ollama
 *       container and wait for it to become reachable.</li>
 *   <li>Ensure the configured model is present; if missing and {@code auto-pull} is enabled,
 *       pull it (streaming progress).</li>
 * </ol>
 * If none of this can be achieved, throws {@link OllamaUnavailableException} with clear guidance
 * rather than letting the command crash.
 */
@Component
public class OllamaProvisioner {

    private static final Logger log = LoggerFactory.getLogger(OllamaProvisioner.class);
    private static final String CONTAINER_NAME = "orvix-ollama";
    private static final String OLLAMA_IMAGE = "ollama/ollama";

    private final OrvixProperties properties;
    private final OllamaClient client;

    public OllamaProvisioner(OrvixProperties properties, OllamaClient client) {
        this.properties = properties;
        this.client = client;
    }

    /** Ensures a reachable Ollama with the configured model. {@code progress} receives status lines. */
    public void ensureReady(Consumer<String> progress) {
        ensureServerReachable(progress);
        ensureModelPresent(progress);
    }

    private void ensureServerReachable(Consumer<String> progress) {
        if (client.isReachable()) {
            return;
        }
        OrvixProperties.Ollama cfg = properties.getOllama();
        if (!cfg.isAutoStart()) {
            throw new OllamaUnavailableException(
                    "Ollama is not reachable at " + cfg.getBaseUrl()
                            + " and auto-start is disabled. Start Ollama and retry.");
        }
        if (!dockerAvailable()) {
            throw new OllamaUnavailableException(
                    "Ollama is not running at " + cfg.getBaseUrl() + ", and Docker is not available "
                            + "to start it automatically.\n"
                            + "Fix it by either:\n"
                            + "  • installing & running Ollama   -> https://ollama.com/download\n"
                            + "  • or installing Docker, then re-running Orvix (it will start Ollama for you).");
        }

        progress.accept("Ollama not running — starting it via Docker...");
        startContainer(progress);

        if (!waitUntilReachable(cfg.getStartupTimeout())) {
            throw new OllamaUnavailableException(
                    "Started the Ollama container but it did not become reachable within "
                            + cfg.getStartupTimeout().toSeconds() + "s.");
        }
        progress.accept("Ollama is up.");
    }

    private void ensureModelPresent(Consumer<String> progress) {
        String model = properties.getOllama().getModel();
        if (client.hasModel(model)) {
            return;
        }
        if (!properties.getOllama().isAutoPull()) {
            throw new OllamaUnavailableException(
                    "Model '" + model + "' is not installed and auto-pull is disabled. "
                            + "Run: ollama pull " + model);
        }
        progress.accept("Model '" + model + "' not found — pulling it (one-time download)...");
        try {
            client.pullModel(model, status -> progress.accept("  " + status));
        } catch (IOException ex) {
            throw new OllamaUnavailableException("Failed to pull model '" + model + "': " + ex.getMessage(), ex);
        }
        if (!client.hasModel(model)) {
            throw new OllamaUnavailableException("Model '" + model + "' still not available after pull.");
        }
        progress.accept("Model '" + model + "' is ready.");
    }

    private void startContainer(Consumer<String> progress) {
        // Reuse an existing container if present; otherwise create one.
        if (runQuietly("docker", "start", CONTAINER_NAME)) {
            progress.accept("Restarted existing Ollama container.");
            return;
        }
        int port = ollamaPort();
        boolean created = runQuietly("docker", "run", "-d",
                "--name", CONTAINER_NAME,
                "-p", port + ":11434",
                "-v", "orvix-ollama:/root/.ollama",
                OLLAMA_IMAGE);
        if (!created) {
            throw new OllamaUnavailableException(
                    "Failed to start the Ollama Docker container. Try manually:\n"
                            + "  docker run -d --name " + CONTAINER_NAME + " -p " + port + ":11434 "
                            + "-v orvix-ollama:/root/.ollama " + OLLAMA_IMAGE);
        }
        progress.accept("Created Ollama container '" + CONTAINER_NAME + "'.");
    }

    private boolean waitUntilReachable(Duration timeout) {
        Instant deadline = Instant.now().plus(timeout);
        while (Instant.now().isBefore(deadline)) {
            if (client.isReachable()) {
                return true;
            }
            sleep(1500);
        }
        return client.isReachable();
    }

    private boolean dockerAvailable() {
        return runQuietly("docker", "--version");
    }

    private int ollamaPort() {
        try {
            int port = URI.create(properties.getOllama().getBaseUrl()).getPort();
            return port > 0 ? port : 11434;
        } catch (RuntimeException ex) {
            return 11434;
        }
    }

    /** Runs a command, discarding output; returns true on exit code 0. Never throws. */
    private boolean runQuietly(String... command) {
        try {
            Process process = new ProcessBuilder(command)
                    .redirectErrorStream(true)
                    .redirectOutput(ProcessBuilder.Redirect.DISCARD)
                    .start();
            if (!process.waitFor(60, TimeUnit.SECONDS)) {
                process.destroyForcibly();
                return false;
            }
            return process.exitValue() == 0;
        } catch (IOException ex) {
            return false;
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    /** Diagnostic helper for the health command: is Docker present? */
    public boolean isDockerAvailable() {
        return dockerAvailable();
    }
}
