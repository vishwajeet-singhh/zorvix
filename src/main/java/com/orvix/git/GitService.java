package com.orvix.git;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.springframework.stereotype.Component;

/**
 * Read-only entry point to the Git repository. Opens the repo containing the working directory
 * and exposes basic metadata. Orvix never mutates the repository through this service.
 */
@Component
public class GitService {

    /**
     * Opens the Git repository that contains {@code startDir} (searching upward for {@code .git}).
     *
     * @return the opened {@link Git}, or empty if {@code startDir} is not inside a repository
     */
    public Optional<Git> open(Path startDir) {
        try {
            FileRepositoryBuilder builder = new FileRepositoryBuilder()
                    .findGitDir(startDir.toFile())
                    .readEnvironment();
            if (builder.getGitDir() == null) {
                return Optional.empty();
            }
            Repository repository = builder.build();
            return Optional.of(new Git(repository));
        } catch (IOException ex) {
            return Optional.empty();
        }
    }

    /** Opens the repository for the current working directory. */
    public Optional<Git> openCurrent() {
        return open(Path.of(System.getProperty("user.dir")));
    }

    /** Current branch name (short form), or {@code "HEAD"} when detached. */
    public String currentBranch(Git git) throws IOException {
        return git.getRepository().getBranch();
    }

    /** Repository name, derived from the working-tree directory. */
    public String repositoryName(Git git) {
        Repository repo = git.getRepository();
        if (repo.isBare() || repo.getWorkTree() == null) {
            return repo.getDirectory() != null ? repo.getDirectory().getName() : "repository";
        }
        return repo.getWorkTree().getName();
    }

    /** Absolute path to the working tree root, used to resolve repo-relative paths. */
    public Path workTree(Git git) {
        return git.getRepository().getWorkTree().toPath();
    }
}
