package com.jmcra.llm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Loads and caches versioned LLM prompt templates from the {@code prompts/} directory.
 *
 * <h3>Template Naming Convention</h3>
 * Each domain has one system prompt template: {@code prompts/<domain-lower>-domain.md}.
 * For example: {@code prompts/sec-domain.md}, {@code prompts/res-domain.md}.
 * <p>
 * Templates support placeholder substitution via {@link #render(String, Map)}.
 * Placeholders use the format {@code {VARIABLE_NAME}}, matching the SDD prompt spec.
 *
 * <h3>Design (ADR-003)</h3>
 * Prompts are stored as versioned Markdown files, never as Java string literals.
 * This allows prompt iteration without code changes and enables prompt diffs in version control.
 * <p>
 * Spec: Section 6.2 (Prompt Architecture):
 * "Prompts are stored as versioned Markdown templates in prompts/ and loaded at runtime.
 *  No prompt literals in Java source."
 */
@Component
public class PromptLoader {

  private static final Logger log = LoggerFactory.getLogger(PromptLoader.class);

  /** In-memory cache: template path → raw template string. */
  private final Map<String, String> cache = new ConcurrentHashMap<>();

  /**
   * Loads the system prompt template for the given domain name.
   *
   * @param domainLower Domain name in lowercase (e.g., "sec", "res", "obs").
   * @return The raw template string (with unresolved placeholders).
   * @throws IllegalStateException if the template file is not found.
   */
  public String loadSystemPrompt(String domainLower) {
    String path = "prompts/" + domainLower + "-domain.md";
    return cache.computeIfAbsent(path, this::readTemplate);
  }

  /**
   * Renders a template by substituting {@code {KEY}} placeholders with values from the map.
   *
   * @param template     Raw template string with {@code {KEY}} placeholders.
   * @param variables    Map of variable names to substitution values.
   * @return The rendered string with all placeholders resolved.
   */
  public String render(String template, Map<String, String> variables) {
    String result = template;
    for (var entry : variables.entrySet()) {
      result = result.replace("{" + entry.getKey() + "}", entry.getValue());
    }
    return result;
  }

  /**
   * Convenience: load and render in one call.
   *
   * @param domainLower  Domain name in lowercase.
   * @param variables    Placeholder substitutions.
   */
  public String loadAndRender(String domainLower, Map<String, String> variables) {
    return render(loadSystemPrompt(domainLower), variables);
  }

  private String readTemplate(String path) {
    // Try classpath first (production mode)
    var resource = new ClassPathResource(path);
    if (resource.exists()) {
      try (InputStream in = resource.getInputStream()) {
        log.debug("Loaded prompt template from classpath: {}", path);
        return new String(in.readAllBytes(), StandardCharsets.UTF_8);
      } catch (IOException e) {
        throw new IllegalStateException("Failed to read prompt template: " + path, e);
      }
    }

    // Fallback: filesystem-relative path (for development / tests)
    var fsPath = java.nio.file.Path.of(path);
    if (java.nio.file.Files.exists(fsPath)) {
      try {
        log.debug("Loaded prompt template from filesystem: {}", path);
        return java.nio.file.Files.readString(fsPath, StandardCharsets.UTF_8);
      } catch (IOException e) {
        throw new IllegalStateException("Failed to read prompt template: " + path, e);
      }
    }

    throw new IllegalStateException(
        "Prompt template not found: " + path +
        " — ensure prompts/ directory is on the classpath or filesystem.");
  }
}
