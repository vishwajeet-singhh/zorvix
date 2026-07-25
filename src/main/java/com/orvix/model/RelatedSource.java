package com.orvix.model;

/**
 * A trimmed snippet of project source pulled in as surrounding context for a changed file
 * (e.g. an implemented interface, a parent class, or a referenced domain type).
 *
 * @param path     repository-relative path of the related source
 * @param relation why it is relevant (e.g. "implements", "extends", "referenced type")
 * @param content  the (possibly truncated) source content
 */
public record RelatedSource(String path, String relation, String content) {
}
