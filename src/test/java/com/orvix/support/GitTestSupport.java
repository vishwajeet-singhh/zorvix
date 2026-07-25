package com.orvix.support;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

/** Helpers for building throwaway Git repositories in tests. */
public final class GitTestSupport {

    private GitTestSupport() {
    }

    public static Git init(Path dir) throws GitAPIException {
        return Git.init().setDirectory(dir.toFile()).setInitialBranch("main").call();
    }

    public static void writeFile(Path repo, String relativePath, String content) throws IOException {
        Path file = repo.resolve(relativePath);
        Files.createDirectories(file.getParent());
        Files.writeString(file, content, StandardCharsets.UTF_8);
    }

    public static void commitAll(Git git, String message) throws GitAPIException {
        git.add().addFilepattern(".").call();
        git.commit().setMessage(message).setAuthor("Test", "test@example.com").call();
    }
}
