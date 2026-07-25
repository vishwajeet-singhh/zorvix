package com.orvix.cli;

import java.util.concurrent.Callable;

import org.springframework.stereotype.Component;

import picocli.CommandLine.Command;

/** {@code orvix version} — prints the Orvix version. */
@Component
@Command(name = "version", mixinStandardHelpOptions = true,
        description = "Print the Orvix version.")
public class VersionCommand implements Callable<Integer> {

    private final OrvixVersionProvider versionProvider;

    public VersionCommand(OrvixVersionProvider versionProvider) {
        this.versionProvider = versionProvider;
    }

    @Override
    public Integer call() {
        System.out.println("orvix " + versionProvider.version());
        return 0;
    }
}
