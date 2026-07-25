package com.orvix.model;

/**
 * A single file changed between the base branch and HEAD.
 *
 * @param path         current repository-relative path ("/dev/null" semantics resolved away)
 * @param oldPath      previous path (differs from {@code path} only for renames/copies)
 * @param changeType   kind of change
 * @param patch        unified diff hunk text for this file (may be empty for binary/deleted)
 * @param addedLines   number of added lines
 * @param deletedLines number of deleted lines
 */
public record ChangedFile(
        String path,
        String oldPath,
        ChangeType changeType,
        String patch,
        int addedLines,
        int deletedLines) {

    public boolean isJava() {
        return path != null && path.endsWith(".java");
    }

    /** Lower-cased file extension without the dot, or empty string if none. */
    public String extension() {
        if (path == null) {
            return "";
        }
        int dot = path.lastIndexOf('.');
        int slash = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
        return dot > slash ? path.substring(dot + 1).toLowerCase() : "";
    }
}
