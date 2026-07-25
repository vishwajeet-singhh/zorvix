package com.orvix.model;

import org.eclipse.jgit.diff.DiffEntry;

/** Kind of change applied to a file in the diff. */
public enum ChangeType {
    ADDED,
    MODIFIED,
    DELETED,
    RENAMED,
    COPIED;

    /** Maps a JGit {@link DiffEntry.ChangeType} onto Orvix's vocabulary. */
    public static ChangeType from(DiffEntry.ChangeType jgit) {
        return switch (jgit) {
            case ADD -> ADDED;
            case DELETE -> DELETED;
            case RENAME -> RENAMED;
            case COPY -> COPIED;
            case MODIFY -> MODIFIED;
        };
    }
}
