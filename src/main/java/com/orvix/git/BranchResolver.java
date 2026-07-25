package com.orvix.git;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.springframework.stereotype.Component;

import com.orvix.config.OrvixProperties;

/**
 * Resolves the base branch a review runs against. Honours an explicit {@code --base} override,
 * otherwise walks the configured priority list and picks the first ref that exists.
 */
@Component
public class BranchResolver {

    private final OrvixProperties properties;

    public BranchResolver(OrvixProperties properties) {
        this.properties = properties;
    }

    /**
     * @param repo     the repository
     * @param override optional explicit base ref (from {@code --base})
     * @return the resolved base branch
     * @throws IllegalStateException if no candidate ref can be resolved
     */
    public BaseBranch resolve(Repository repo, Optional<String> override) {
        List<String> candidates = candidateNames(override);
        for (String name : candidates) {
            Optional<BaseBranch> resolved = tryResolve(repo, name);
            if (resolved.isPresent()) {
                return resolved.get();
            }
        }
        if (override.isPresent()) {
            throw new IllegalStateException("Base branch '" + override.get() + "' could not be resolved.");
        }
        throw new IllegalStateException(
                "No base branch found. Tried: " + String.join(", ", candidates)
                        + ". Specify one explicitly with --base <branch>.");
    }

    /** Ordered, de-duplicated candidate names to attempt. */
    private List<String> candidateNames(Optional<String> override) {
        Set<String> names = new LinkedHashSet<>();
        if (override.isPresent() && !override.get().isBlank()) {
            String base = override.get().trim();
            names.add(base);
            names.add("origin/" + base);
            names.add("refs/heads/" + base);
            names.add("refs/remotes/origin/" + base);
        } else {
            names.addAll(properties.getReview().getBaseBranchPriority());
        }
        return List.copyOf(names);
    }

    private Optional<BaseBranch> tryResolve(Repository repo, String name) {
        try {
            ObjectId id = repo.resolve(name + "^{commit}");
            if (id == null) {
                id = repo.resolve(name);
            }
            return Optional.ofNullable(id).map(commit -> new BaseBranch(name, commit));
        } catch (IOException ex) {
            return Optional.empty();
        }
    }
}
