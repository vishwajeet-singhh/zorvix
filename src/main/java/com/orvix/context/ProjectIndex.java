package com.orvix.context;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * An immutable index of the project's Java types: fully-qualified name → file, plus a
 * simple-name → candidate-FQNs lookup and a compact human-readable structure summary.
 */
public final class ProjectIndex {

    private final Map<String, Path> fqnToPath;
    private final Map<String, List<String>> simpleNameToFqns;
    private final String structureSummary;

    public ProjectIndex(Map<String, Path> fqnToPath,
                        Map<String, List<String>> simpleNameToFqns,
                        String structureSummary) {
        this.fqnToPath = Map.copyOf(fqnToPath);
        this.simpleNameToFqns = Map.copyOf(simpleNameToFqns);
        this.structureSummary = structureSummary;
    }

    /** Resolves a fully-qualified type name to its source file, if it belongs to the project. */
    public Optional<Path> byFqn(String fqn) {
        return Optional.ofNullable(fqnToPath.get(fqn));
    }

    /** Resolves a simple type name to candidate project source files. */
    public List<Path> bySimpleName(String simpleName) {
        List<String> fqns = simpleNameToFqns.getOrDefault(simpleName, List.of());
        return fqns.stream().map(fqnToPath::get).filter(p -> p != null).toList();
    }

    public boolean isProjectType(String fqn) {
        return fqnToPath.containsKey(fqn);
    }

    public int size() {
        return fqnToPath.size();
    }

    public String structureSummary() {
        return structureSummary;
    }
}
