package com.orvix.cli;

import java.util.concurrent.Callable;

import org.springframework.stereotype.Component;

import com.orvix.ai.OllamaUnavailableException;
import com.orvix.review.AssistantService;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

/** {@code orvix explain <file>} — explains a single source file. */
@Component
@Command(name = "explain", mixinStandardHelpOptions = true,
        description = "Explain a source file: responsibility, dependencies, risks, design.")
public class ExplainCommand implements Callable<Integer> {

    @Parameters(index = "0", arity = "1", description = "Path to the source file to explain.")
    private String file;

    private final AssistantService assistantService;

    public ExplainCommand(AssistantService assistantService) {
        this.assistantService = assistantService;
    }

    @Override
    public Integer call() {
        try {
            String explanation = assistantService.explain(file, msg -> System.err.println("• " + msg));
            System.out.println();
            System.out.println(explanation.trim());
            return 0;
        } catch (IllegalArgumentException ex) {
            System.err.println("\n" + ex.getMessage());
            return 2;
        } catch (OllamaUnavailableException ex) {
            System.err.println("\n" + ex.getMessage());
            return 1;
        } catch (Exception ex) {
            System.err.println("\nExplain failed: " + ex.getMessage());
            return 1;
        }
    }
}
