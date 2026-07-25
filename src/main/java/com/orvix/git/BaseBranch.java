package com.orvix.git;

import org.eclipse.jgit.lib.ObjectId;

/**
 * A resolved base branch: the ref name that matched and the commit it points to.
 *
 * @param refName the resolvable ref name (e.g. "origin/main")
 * @param commit  the commit object id it resolves to
 */
public record BaseBranch(String refName, ObjectId commit) {
}
