package com.orvix.git;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.file.Path;
import java.util.Optional;

import org.eclipse.jgit.api.Git;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.orvix.config.OrvixProperties;
import com.orvix.support.GitTestSupport;

class BranchResolverTest {

    private final BranchResolver resolver = new BranchResolver(new OrvixProperties());

    @Test
    void resolvesLocalMainFromPriorityList(@TempDir Path dir) throws Exception {
        try (Git git = GitTestSupport.init(dir)) {
            GitTestSupport.writeFile(dir, "a.txt", "hello");
            GitTestSupport.commitAll(git, "init");
            git.checkout().setCreateBranch(true).setName("feature").call();

            BaseBranch base = resolver.resolve(git.getRepository(), Optional.empty());

            assertThat(base.refName()).isEqualTo("main");
            assertThat(base.commit()).isNotNull();
        }
    }

    @Test
    void honoursExplicitOverride(@TempDir Path dir) throws Exception {
        try (Git git = GitTestSupport.init(dir)) {
            GitTestSupport.writeFile(dir, "a.txt", "hello");
            GitTestSupport.commitAll(git, "init");

            BaseBranch base = resolver.resolve(git.getRepository(), Optional.of("main"));

            assertThat(base.refName()).isEqualTo("main");
        }
    }

    @Test
    void throwsWhenOverrideUnresolvable(@TempDir Path dir) throws Exception {
        try (Git git = GitTestSupport.init(dir)) {
            GitTestSupport.writeFile(dir, "a.txt", "hello");
            GitTestSupport.commitAll(git, "init");

            assertThatThrownBy(() -> resolver.resolve(git.getRepository(), Optional.of("nope")))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("nope");
        }
    }
}
