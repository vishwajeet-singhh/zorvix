package com.orvix.context;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.orvix.config.OrvixProperties;
import com.orvix.model.ChangedFile;
import com.orvix.model.CodeContext;
import com.orvix.model.Finding;
import com.orvix.model.RelatedSource;
import com.orvix.parse.JavaSourceParser;
import com.orvix.parse.ParsedJava;

/**
 * Builds the surrounding-code context for a review: for each changed Java file it resolves the
 * project types it depends on (supertypes, imported project classes) and pulls in trimmed source
 * for them — staying within a configured character budget so the whole repo is never sent.
 */
@Component
public class ContextBuilder {

    private final OrvixProperties properties;
    private final JavaSourceParser parser;

    public ContextBuilder(OrvixProperties properties, JavaSourceParser parser) {
        this.properties = properties;
        this.parser = parser;
    }

    /** Assembles the full {@link CodeContext} including static-analysis findings. */
    public CodeContext assemble(String repoName, String branch, String baseBranch,
                                List<ChangedFile> changedFiles, Path workTree,
                                ProjectIndex index, List<Finding> staticFindings) {
        List<RelatedSource> related = relatedSources(changedFiles, workTree, index);
        return new CodeContext(repoName, branch, baseBranch, changedFiles, related,
                index.structureSummary(), staticFindings);
    }

    /** Resolves and loads trimmed related project source for the changed files, within budget. */
    public List<RelatedSource> relatedSources(List<ChangedFile> changedFiles, Path workTree,
                                              ProjectIndex index) {
        int maxFiles = properties.getReview().getMaxRelatedFiles();
        int budget = properties.getReview().getMaxContextChars();
        int perFileCap = maxFiles > 0 ? Math.max(800, budget / maxFiles) : budget;

        // Paths already covered by the diff itself — never duplicate them as "related".
        List<String> changedPaths = changedFiles.stream().map(ChangedFile::path).toList();

        // Preserve insertion order and de-duplicate by resolved path.
        Map<Path, String> selected = new LinkedHashMap<>();
        for (ChangedFile cf : changedFiles) {
            if (!cf.isJava()) {
                continue;
            }
            String source = readSource(workTree, cf.path());
            if (source == null) {
                continue;
            }
            ParsedJava parsed = parser.parse(source);
            collectRelated(parsed, index, changedPaths, selected);
        }

        List<RelatedSource> result = new ArrayList<>();
        int used = 0;
        for (Map.Entry<Path, String> entry : selected.entrySet()) {
            if (result.size() >= maxFiles || used >= budget) {
                break;
            }
            String content = readSource(workTree, workTree.relativize(entry.getKey()).toString());
            if (content == null) {
                continue;
            }
            String trimmed = truncate(content, Math.min(perFileCap, budget - used));
            used += trimmed.length();
            result.add(new RelatedSource(
                    workTree.relativize(entry.getKey()).toString(), entry.getValue(), trimmed));
        }
        return result;
    }

    private void collectRelated(ParsedJava parsed, ProjectIndex index, List<String> changedPaths,
                                Map<Path, String> selected) {
        // Supertypes first (highest review value), then imported project types.
        for (String supertype : parsed.supertypes()) {
            for (Path path : index.bySimpleName(supertype)) {
                addCandidate(selected, path, "supertype/interface", changedPaths);
            }
        }
        for (String imported : parsed.imports()) {
            if (index.isProjectType(imported)) {
                index.byFqn(imported).ifPresent(
                        path -> addCandidate(selected, path, "referenced project type", changedPaths));
            }
        }
    }

    private void addCandidate(Map<Path, String> selected, Path path, String relation,
                              List<String> changedPaths) {
        // Skip files that are themselves part of the diff.
        String asString = path.toString();
        boolean isChanged = changedPaths.stream().anyMatch(asString::endsWith);
        if (!isChanged) {
            selected.putIfAbsent(path, relation);
        }
    }

    private String readSource(Path workTree, String relativePath) {
        if (relativePath == null || relativePath.isBlank()) {
            return null;
        }
        try {
            Path file = workTree.resolve(relativePath);
            if (!Files.isRegularFile(file)) {
                return null;
            }
            return Files.readString(file, StandardCharsets.UTF_8);
        } catch (IOException | RuntimeException ex) {
            return null;
        }
    }

    private String truncate(String content, int cap) {
        if (cap <= 0) {
            return "";
        }
        if (content.length() <= cap) {
            return content;
        }
        return content.substring(0, cap) + "\n// ... (truncated for context budget)\n";
    }
}
