package com.orvix.cli;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

import picocli.CommandLine.IVersionProvider;

/** Supplies the version string for {@code --version}, sourced from Spring's build-info. */
@Component
public class OrvixVersionProvider implements IVersionProvider {

    private final ObjectProvider<BuildProperties> buildProperties;

    public OrvixVersionProvider(ObjectProvider<BuildProperties> buildProperties) {
        this.buildProperties = buildProperties;
    }

    public String version() {
        BuildProperties props = buildProperties.getIfAvailable();
        return props != null ? props.getVersion() : "dev";
    }

    @Override
    public String[] getVersion() {
        return new String[] {"orvix " + version()};
    }
}
