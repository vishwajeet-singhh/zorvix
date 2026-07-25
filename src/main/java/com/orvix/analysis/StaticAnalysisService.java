package com.orvix.analysis;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.orvix.model.ChangedFile;
import com.orvix.model.Finding;

/**
 * Runs every available {@link StaticAnalyzer} over the changed files and aggregates their
 * findings. Analyzers are discovered as Spring beans, so new backends require no changes here.
 */
@Service
public class StaticAnalysisService {

    private static final Logger log = LoggerFactory.getLogger(StaticAnalysisService.class);

    private final List<StaticAnalyzer> analyzers;

    public StaticAnalysisService(List<StaticAnalyzer> analyzers) {
        this.analyzers = analyzers;
    }

    public List<Finding> analyze(Path workTree, List<ChangedFile> changedFiles) {
        List<Finding> all = new ArrayList<>();
        for (StaticAnalyzer analyzer : analyzers) {
            if (!analyzer.isAvailable()) {
                log.debug("Static analyzer {} not available — skipping", analyzer.name());
                continue;
            }
            List<Finding> findings = analyzer.analyze(workTree, changedFiles);
            log.info("{} produced {} finding(s)", analyzer.name(), findings.size());
            all.addAll(findings);
        }
        return all;
    }

    /** Names of analyzers currently available, for the health command. */
    public List<String> availableAnalyzers() {
        return analyzers.stream().filter(StaticAnalyzer::isAvailable).map(StaticAnalyzer::name).toList();
    }
}
