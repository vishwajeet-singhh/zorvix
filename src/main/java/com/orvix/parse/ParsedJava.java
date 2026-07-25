package com.orvix.parse;

import java.util.List;

/**
 * The structural facts extracted from a single Java source file, used to resolve relevant
 * surrounding context for a review.
 *
 * @param packageName fully-qualified package, or empty string for the default package
 * @param typeNames   simple names of top-level types declared in the file
 * @param imports     explicit (non-asterisk) imported fully-qualified names
 * @param supertypes  simple names of extended/implemented types
 * @param methodNames declared method names
 */
public record ParsedJava(
        String packageName,
        List<String> typeNames,
        List<String> imports,
        List<String> supertypes,
        List<String> methodNames) {

    public static ParsedJava empty() {
        return new ParsedJava("", List.of(), List.of(), List.of(), List.of());
    }
}
