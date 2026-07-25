package com.orvix.git;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import org.springframework.stereotype.Component;

import com.orvix.model.ChangeType;
import com.orvix.model.ChangedFile;

/**
 * Computes the set of changed files for {@code base...HEAD} — i.e. everything on the current
 * branch since it diverged from the base (three-dot semantics, using the merge-base). Read-only.
 */
@Component
public class DiffAnalyzer {

    private static final String DEV_NULL = "/dev/null";

    /**
     * @param git     the open repository
     * @param baseRef resolved base ref name (e.g. "origin/main")
     * @return changed files with per-file unified-diff patches
     */
    public List<ChangedFile> changedFiles(Git git, String baseRef) throws IOException {
        Repository repo = git.getRepository();
        ObjectId headId = repo.resolve("HEAD^{commit}");
        ObjectId baseId = repo.resolve(baseRef + "^{commit}");
        if (headId == null) {
            throw new IllegalStateException("HEAD has no commits to review.");
        }
        if (baseId == null) {
            throw new IllegalStateException("Base ref '" + baseRef + "' could not be resolved to a commit.");
        }

        try (RevWalk revWalk = new RevWalk(repo)) {
            RevCommit head = revWalk.parseCommit(headId);
            RevCommit base = revWalk.parseCommit(baseId);
            RevCommit mergeBase = findMergeBase(repo, head, base);
            // Three-dot: diff from merge-base tree to HEAD tree.
            ObjectId fromTree = (mergeBase != null ? mergeBase : base).getTree();
            return diffTrees(repo, fromTree, head.getTree());
        }
    }

    private RevCommit findMergeBase(Repository repo, RevCommit a, RevCommit b) throws IOException {
        try (RevWalk walk = new RevWalk(repo)) {
            walk.setRevFilter(RevFilter.MERGE_BASE);
            walk.markStart(walk.parseCommit(a));
            walk.markStart(walk.parseCommit(b));
            return walk.next();
        }
    }

    private List<ChangedFile> diffTrees(Repository repo, ObjectId fromTree, ObjectId toTree) throws IOException {
        List<ChangedFile> result = new ArrayList<>();
        try (ObjectReader reader = repo.newObjectReader();
             ByteArrayOutputStream out = new ByteArrayOutputStream();
             DiffFormatter formatter = new DiffFormatter(DisabledOutputStream.INSTANCE)) {

            AbstractTreeIterator oldTree = treeIterator(reader, fromTree);
            AbstractTreeIterator newTree = treeIterator(reader, toTree);

            formatter.setRepository(repo);
            formatter.setDetectRenames(true);

            List<DiffEntry> entries = formatter.scan(oldTree, newTree);
            for (DiffEntry entry : entries) {
                result.add(toChangedFile(repo, entry));
            }
        }
        return result;
    }

    private ChangedFile toChangedFile(Repository repo, DiffEntry entry) throws IOException {
        ChangeType changeType = ChangeType.from(entry.getChangeType());
        String newPath = normalise(entry.getNewPath());
        String oldPath = normalise(entry.getOldPath());
        String path = (changeType == ChangeType.DELETED) ? oldPath : newPath;

        int added = 0;
        int deleted = 0;
        String patch = "";
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             DiffFormatter formatter = new DiffFormatter(out)) {
            formatter.setRepository(repo);
            formatter.setDetectRenames(true);
            formatter.format(entry);
            patch = out.toString(StandardCharsets.UTF_8);
            for (Edit edit : formatter.toFileHeader(entry).toEditList()) {
                added += edit.getEndB() - edit.getBeginB();
                deleted += edit.getEndA() - edit.getBeginA();
            }
        } catch (IOException ex) {
            // Binary or unreadable content: keep counts at zero and patch empty.
            patch = "";
        }

        return new ChangedFile(path, oldPath, changeType, patch, added, deleted);
    }

    private static String normalise(String path) {
        return DEV_NULL.equals(path) ? "" : path;
    }

    private static AbstractTreeIterator treeIterator(ObjectReader reader, ObjectId treeId) throws IOException {
        CanonicalTreeParser parser = new CanonicalTreeParser();
        parser.reset(reader, treeId);
        return parser;
    }
}
