package com.orvix.ai;

/**
 * Raised when Orvix cannot reach (or provision) a working Ollama runtime with the required model.
 * Carries user-facing guidance on how to resolve it.
 */
public class OllamaUnavailableException extends RuntimeException {

    public OllamaUnavailableException(String message) {
        super(message);
    }

    public OllamaUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
