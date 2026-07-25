package com.orvix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Orvix entry point. Runs as a CLI (no web server). The Spring context provides DI and config;
 * {@link CliRunner} drives picocli and supplies the process exit code.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class Orvix {

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(
                new SpringApplicationBuilder(Orvix.class)
                        .web(WebApplicationType.NONE)
                        .run(args)));
    }
}
