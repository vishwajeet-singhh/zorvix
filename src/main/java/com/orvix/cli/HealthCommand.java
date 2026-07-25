package com.orvix.cli;

import java.util.concurrent.Callable;

import org.springframework.stereotype.Component;

import com.orvix.ai.OllamaClient;
import com.orvix.ai.OllamaProvisioner;
import com.orvix.analysis.StaticAnalysisService;
import com.orvix.config.OrvixProperties;
import com.orvix.git.GitService;

import picocli.CommandLine.Command;

/** {@code orvix health} — validates the environment and prints a status table. */
@Component
@Command(name = "health", mixinStandardHelpOptions = true,
        description = "Check Orvix's environment: git, Docker, Ollama, model.")
public class HealthCommand implements Callable<Integer> {

    private final GitService gitService;
    private final OllamaClient ollamaClient;
    private final OllamaProvisioner provisioner;
    private final StaticAnalysisService staticAnalysisService;
    private final OrvixProperties properties;

    public HealthCommand(GitService gitService, OllamaClient ollamaClient, OllamaProvisioner provisioner,
                         StaticAnalysisService staticAnalysisService, OrvixProperties properties) {
        this.gitService = gitService;
        this.ollamaClient = ollamaClient;
        this.provisioner = provisioner;
        this.staticAnalysisService = staticAnalysisService;
        this.properties = properties;
    }

    @Override
    public Integer call() {
        boolean repo = gitService.openCurrent().isPresent();
        boolean docker = provisioner.isDockerAvailable();
        boolean ollamaUp = ollamaClient.isReachable();
        String ollamaVersion = ollamaUp ? ollamaClient.version() : "";
        String model = properties.getOllama().getModel();
        boolean modelInstalled = ollamaUp && ollamaClient.hasModel(model);

        System.out.println();
        System.out.println("ORVIX HEALTH");
        System.out.println();
        row("Git repository detected", repo, repo ? "yes" : "run inside a git repo");
        row("Docker available", docker, docker ? "yes" : "optional (used to auto-start Ollama)");
        row("Ollama running", ollamaUp,
                ollamaUp ? ("yes (v" + ollamaVersion + ") @ " + properties.getOllama().getBaseUrl())
                        : "not reachable");
        if (ollamaUp) {
            row("Model installed", modelInstalled,
                    modelInstalled ? model : (model + " (will be pulled on first use)"));
        } else {
            row("Model installed", false, "unknown until Ollama is reachable");
        }
        row("Static analyzers", !staticAnalysisService.availableAnalyzers().isEmpty(),
                String.join(", ", staticAnalysisService.availableAnalyzers()));

        System.out.println();
        boolean aiUsable = ollamaUp || docker;
        if (!aiUsable) {
            System.out.println("AI review is unavailable: install Ollama (https://ollama.com/download) "
                    + "or Docker so Orvix can start it for you.");
            return 1;
        }
        System.out.println("Ready.");
        return 0;
    }

    private void row(String label, boolean ok, String detail) {
        String mark = ok ? "[ OK ]" : "[WARN]";
        System.out.printf("  %s  %-26s %s%n", mark, label, detail == null ? "" : detail);
    }
}
