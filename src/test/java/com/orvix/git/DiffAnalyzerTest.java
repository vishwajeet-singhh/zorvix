package com.orvix.git;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.orvix.model.ChangeType;
import com.orvix.model.ChangedFile;
import com.orvix.support.GitTestSupport;

class DiffAnalyzerTest {

    private final DiffAnalyzer analyzer = new DiffAnalyzer();

    @Test
    void detectsAddedAndModifiedFilesOnFeatureBranch(@TempDir Path dir) throws Exception {
        try (Git git = GitTestSupport.init(dir)) {
            GitTestSupport.writeFile(dir, "src/A.java", "class A { int x; }\n");
            GitTestSupport.commitAll(git, "base");

            git.checkout().setCreateBranch(true).setName("feature").call();
            GitTestSupport.writeFile(dir, "src/A.java", "class A { int x; int y; }\n");
            GitTestSupport.writeFile(dir, "src/B.java", "class B { }\n");
            GitTestSupport.commitAll(git, "feature work");

            List<ChangedFile> changed = analyzer.changedFiles(git, "main");

            assertThat(changed).hasSize(2);
            assertThat(changed).anySatisfy(cf -> {
                assertThat(cf.path()).isEqualTo("src/A.java");
                assertThat(cf.changeType()).isEqualTo(ChangeType.MODIFIED);
            });
            assertThat(changed).anySatisfy(cf -> {
                assertThat(cf.path()).isEqualTo("src/B.java");
                assertThat(cf.changeType()).isEqualTo(ChangeType.ADDED);
            });
            // Modified file should carry a textual patch with the added line.
            ChangedFile modified = changed.stream()
                    .filter(cf -> cf.path().equals("src/A.java")).findFirst().orElseThrow();
            assertThat(modified.patch()).contains("int y");
            assertThat(modified.addedLines()).isGreaterThan(0);
        }
    }

    @Test
    void noChangesWhenBranchMatchesBase(@TempDir Path dir) throws Exception {
        try (Git git = GitTestSupport.init(dir)) {
            GitTestSupport.writeFile(dir, "src/A.java", "class A { }\n");
            GitTestSupport.commitAll(git, "base");

            List<ChangedFile> changed = analyzer.changedFiles(git, "main");

            assertThat(changed).isEmpty();
        }
    }
}
