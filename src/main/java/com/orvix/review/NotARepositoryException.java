package com.orvix.review;

/** Raised when an Orvix command is run outside a Git repository. */
public class NotARepositoryException extends RuntimeException {

    public NotARepositoryException(String message) {
        super(message);
    }
}
