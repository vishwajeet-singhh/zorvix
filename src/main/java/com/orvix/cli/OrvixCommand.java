package com.orvix.cli;

import java.util.concurrent.Callable;

import org.springframework.stereotype.Component;

import com.orvix.config.OrvixProperties;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ScopeType;

/**
 * Root command. Holds the subcommands; running {@code orvix} with no subcommand prints usage.
 */
@Component
@Command(
        name = "orvix",
        mixinStandardHelpOptions = true,
        versionProvider = OrvixVersionProvider.class,
        description = "Orvix — local-first AI code review assistant. Reviews your branch before you open a PR.",
        subcommands = {
                ReviewCommand.class,
                AskCommand.class,
                ExplainCommand.class,
                ChatCommand.class,
                HealthCommand.class,
                VersionCommand.class
        })
public class OrvixCommand implements Callable<Integer> {

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    private final OrvixProperties properties;

    public OrvixCommand(OrvixProperties properties) {
        this.properties = properties;
    }

    /**
     * Global override for the Ollama model, usable on any subcommand (e.g.
     * {@code orvix review --model qwen2.5-coder:3b}). Applied to the shared properties before any
     * service runs, so it takes effect without editing configuration. Handy for A/B-testing models.
     */
    @Option(names = {"--model", "-m"}, scope = ScopeType.INHERIT, paramLabel = "<name:tag>",
            description = "Override the Ollama model for this run (e.g. qwen2.5-coder:3b).")
    public void setModelOverride(String model) {
        if (model != null && !model.isBlank()) {
            properties.getOllama().setModel(model.trim());
        }
    }

    @Override
    public Integer call() {
        spec.commandLine().usage(System.out);
        return 0;
    }
}
