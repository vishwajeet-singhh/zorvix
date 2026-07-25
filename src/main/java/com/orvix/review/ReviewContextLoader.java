package com.orvix.review;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.eclipse.jgit.api.Git;
import org.springframework.stereotype.Service;

import com.orvix.analysis.StaticAnalysisService;
import com.orvix.context.ContextBuilder;
import com.orvix.context.ProjectIndex;
import com.orvix.context.ProjectIndexer;
import com.orvix.git.BaseBranch;
import com.orvix.git.BranchResolver;
import com.orvix.git.DiffAnalyzer;
import com.orvix.git.GitService;
import com.orvix.model.ChangedFile;
import com.orvix.model.CodeContext;
import com.orvix.model.Finding;

/**
 * Shared pipeline that turns "the current repository state" into a {@link LoadedContext}:
 * open repo → detect branch → resolve base → diff → index project → build surrounding context →
 * (optionally) run static analysis. Used by both review and ask/explain so the heavy lifting
 * lives in one place.
 */
@Service
public class ReviewContextLoader {

    private final GitService gitService;
    private final BranchResolver branchResolver;
    private final DiffAnalyzer diffAnalyzer;
    private final ProjectIndexer projectIndexer;
    private final ContextBuilder contextBuilder;
    private final StaticAnalysisService staticAnalysisService;

    public ReviewContextLoader(GitService gitService, BranchResolver branchResolver,
                               DiffAnalyzer diffAnalyzer, ProjectIndexer projectIndexer,
                               ContextBuilder contextBuilder, StaticAnalysisService staticAnalysisService) {
        this.gitService = gitService;
        this.branchResolver = branchResolver;
        this.diffAnalyzer = diffAnalyzer;
        this.projectIndexer = projectIndexer;
        this.contextBuilder = contextBuilder;
        this.staticAnalysisService = staticAnalysisService;
    }

    /**
     * @param baseOverride explicit base branch (from {@code --base}), if any
     * @param runStatic    whether to run static analysis (review yes; ask/explain no)
     * @param progress     receives human-readable progress messages
     * @throws NotARepositoryException if the working directory is not inside a Git repository
     */
    public LoadedContext load(Optional<String> baseOverride, boolean runStatic, Consumer<String> progress) {
        Git git = gitService.openCurrent()
                .orElseThrow(() -> new NotARepositoryException(
                        "Not a Git repository: " + System.getProperty("user.dir")));
        try {
            String repoName = gitService.repositoryName(git);
            String branch = gitService.currentBranch(git);
            Path workTree = gitService.workTree(git);

            BaseBranch base = branchResolver.resolve(git.getRepository(), baseOverride);
            progress.accept("Comparing " + branch + " against base " + base.refName());

            List<ChangedFile> changed = diffAnalyzer.changedFiles(git, base.refName());
            progress.accept(changed.size() + " changed file(s) detected");

            progress.accept("Indexing project for context...");
            ProjectIndex index = projectIndexer.index(workTree);

            List<Finding> staticFindings = List.of();
            if (runStatic && !changed.isEmpty()) {
                progress.accept("Running static analysis...");
                staticFindings = staticAnalysisService.analyze(workTree, changed);
            }

            CodeContext context = contextBuilder.assemble(
                    repoName, branch, base.refName(), changed, workTree, index, staticFindings);
            return new LoadedContext(workTree, context);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to analyze repository: " + ex.getMessage(), ex);
        } finally {
            git.close();
        }
    }
}
