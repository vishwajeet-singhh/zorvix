package com.orvix;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;

import com.orvix.cli.OrvixCommand;

import picocli.CommandLine;

/**
 * Bridges Spring Boot startup to picocli: executes the command line using a Spring-backed factory
 * and exposes the resulting exit code to {@code SpringApplication.exit(...)}.
 */
@Component
public class CliRunner implements ApplicationRunner, ExitCodeGenerator {

    private final OrvixCommand rootCommand;
    private final SpringPicocliFactory factory;
    private int exitCode;

    public CliRunner(OrvixCommand rootCommand, SpringPicocliFactory factory) {
        this.rootCommand = rootCommand;
        this.factory = factory;
    }

    @Override
    public void run(ApplicationArguments args) {
        this.exitCode = new CommandLine(rootCommand, factory).execute(args.getSourceArgs());
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }
}
