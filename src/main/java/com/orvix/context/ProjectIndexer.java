package com.orvix.context;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Walks a repository's working tree and builds a {@link ProjectIndex} of its Java types.
 *
 * <p>Optimized for repeated runs: types are extracted with a fast regex scan (no full AST parse),
 * and results are cached per file in {@code .orvix/cache/index.json} keyed by modification time
 * and size — so unchanged files are never re-read.
 */
@Component
public class ProjectIndexer {

    private static final Logger log = LoggerFactory.getLogger(ProjectIndexer.class);

    private static final Set<String> IGNORED_DIRS = Set.of(
            ".git", ".gradle", ".idea", ".orvix", "build", "target", "out", "bin",
            "node_modules", ".mvn", "dist");
    private static final int MAX_FILES = 8_000;
    private static final String CACHE_PATH = ".orvix/cache/index.json";

    private static final Pattern PACKAGE = Pattern.compile("(?m)^\\s*package\\s+([\\w.]+)\\s*;");
    // Top-level type declarations: keyword preceded only by modifiers at the start of a line.
    private static final Pattern TYPE = Pattern.compile(
            "(?m)^[\\t ]*(?:(?:public|private|protected|abstract|final|sealed|non-sealed|strictfp|static)\\s+)*"
                    + "(?:class|interface|enum|record)\\s+(\\w+)");

    private final ObjectMapper mapper;

    public ProjectIndexer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /** A cached scan result for a single file. */
    public record CacheEntry(long mtime, long size, String pkg, List<String> types) {
    }

    public ProjectIndex index(Path workTree) {
        Map<String, CacheEntry> cache = loadCache(workTree);
        Map<String, CacheEntry> fresh = new HashMap<>();

        Map<String, Path> fqnToPath = new HashMap<>();
        Map<String, List<String>> simpleToFqns = new HashMap<>();
        Map<String, Integer> packageCounts = new TreeMap<>();
        int[] count = {0};

        try (Stream<Path> stream = Files.walk(workTree)) {
            stream.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .filter(p -> !isIgnored(workTree, p))
                    .limit(MAX_FILES)
                    .forEach(p -> indexFile(workTree, p, cache, fresh,
                            fqnToPath, simpleToFqns, packageCounts, count));
        } catch (IOException ex) {
            log.debug("Project walk failed: {}", ex.getMessage());
        }

        saveCache(workTree, fresh);
        return new ProjectIndex(fqnToPath, simpleToFqns, summarise(packageCounts, count[0]));
    }

    private void indexFile(Path workTree, Path file,
                           Map<String, CacheEntry> cache, Map<String, CacheEntry> fresh,
                           Map<String, Path> fqnToPath, Map<String, List<String>> simpleToFqns,
                           Map<String, Integer> packageCounts, int[] count) {
        String rel = workTree.relativize(file).toString();
        try {
            BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);
            long mtime = attrs.lastModifiedTime().toMillis();
            long size = attrs.size();

            CacheEntry entry = cache.get(rel);
            if (entry == null || entry.mtime() != mtime || entry.size() != size) {
                entry = scan(file, mtime, size);
            }
            fresh.put(rel, entry);

            String pkg = entry.pkg();
            packageCounts.merge(pkg.isEmpty() ? "(default)" : pkg, 1, Integer::sum);
            for (String type : entry.types()) {
                String fqn = pkg.isEmpty() ? type : pkg + "." + type;
                fqnToPath.put(fqn, file);
                simpleToFqns.computeIfAbsent(type, k -> new ArrayList<>()).add(fqn);
            }
            count[0]++;
        } catch (IOException | RuntimeException ex) {
            log.debug("Skipping {}: {}", rel, ex.getMessage());
        }
    }

    /** Fast regex scan of a file for its package and top-level type names. */
    private CacheEntry scan(Path file, long mtime, long size) throws IOException {
        String source = Files.readString(file, StandardCharsets.UTF_8);

        Matcher pkgMatcher = PACKAGE.matcher(source);
        String pkg = pkgMatcher.find() ? pkgMatcher.group(1) : "";

        List<String> types = new ArrayList<>();
        Matcher typeMatcher = TYPE.matcher(source);
        while (typeMatcher.find()) {
            types.add(typeMatcher.group(1));
        }
        return new CacheEntry(mtime, size, pkg, types);
    }

    private boolean isIgnored(Path workTree, Path file) {
        Path relative = workTree.relativize(file);
        for (Path segment : relative) {
            if (IGNORED_DIRS.contains(segment.toString())) {
                return true;
            }
        }
        return false;
    }

    private Map<String, CacheEntry> loadCache(Path workTree) {
        Path cacheFile = workTree.resolve(CACHE_PATH);
        if (!Files.isRegularFile(cacheFile)) {
            return Map.of();
        }
        try {
            return mapper.readValue(cacheFile.toFile(), new TypeReference<Map<String, CacheEntry>>() {});
        } catch (IOException | RuntimeException ex) {
            log.debug("Index cache unreadable, rebuilding: {}", ex.getMessage());
            return Map.of();
        }
    }

    private void saveCache(Path workTree, Map<String, CacheEntry> fresh) {
        Path cacheFile = workTree.resolve(CACHE_PATH);
        try {
            Files.createDirectories(cacheFile.getParent());
            mapper.writeValue(cacheFile.toFile(), fresh);
        } catch (IOException | RuntimeException ex) {
            log.debug("Could not write index cache: {}", ex.getMessage());
        }
    }

    private String summarise(Map<String, Integer> packageCounts, int fileCount) {
        StringBuilder sb = new StringBuilder();
        sb.append("Java files indexed: ").append(fileCount).append('\n');
        sb.append("Packages (").append(packageCounts.size()).append("):\n");
        packageCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(40)
                .forEach(e -> sb.append("  - ").append(e.getKey())
                        .append(" (").append(e.getValue()).append(" types)\n"));
        return sb.toString();
    }
}
