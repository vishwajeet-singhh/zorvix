package com.orvix.review;

import java.nio.file.Path;

import com.orvix.model.CodeContext;

/**
 * The fully-assembled inputs for an AI command: the repository work tree (for reading additional
 * files, e.g. {@code explain}) and the {@link CodeContext} handed to the model.
 */
public record LoadedContext(Path workTree, CodeContext context) {
}
