package com.jmcra.rules.catalog;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmcra.model.Domain;
import com.jmcra.model.Severity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Loads and validates {@code rules/catalog.json} at application startup.
 *
 * <h3>Catalog Integrity Guard</h3>
 * The catalog file is SHA-256 hashed at load time. If the hash does not match
 * the value stored in {@code rules/catalog.sha256}, a {@link CatalogIntegrityException}
 * is thrown and the application fails to start. Any change to a rule's ID, description,
 * or defaultSeverity requires updating {@code catalog.sha256} via a formal CHANGELOG entry.
 * <p>
 * Spec: Section 7A.5 (Spec Drift Detection — Rule Catalog Hash Guard).
 */
@Component
public class RuleCatalogLoader {

  private static final Logger log = LoggerFactory.getLogger(RuleCatalogLoader.class);

  private static final String CATALOG_PATH     = "rules/catalog.json";
  private static final String CATALOG_HASH_PATH = "rules/catalog.sha256";

  private final ObjectMapper objectMapper;
  private final List<CatalogRule> rules;

  public RuleCatalogLoader(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    this.rules = loadAndValidate();
    log.info("Rule catalog loaded: {} rules ({} enabled)",
        rules.size(), rules.stream().filter(CatalogRule::enabled).count());
  }

  // ── Public API ─────────────────────────────────────────────────────────────

  /** Returns all rules in the catalog (enabled and disabled). */
  public List<CatalogRule> allRules() {
    return List.copyOf(rules);
  }

  /** Returns only enabled rules (default-profile active rules). */
  public List<CatalogRule> enabledRules() {
    return rules.stream().filter(CatalogRule::enabled).toList();
  }

  /** Returns enabled rules for the given domain. */
  public List<CatalogRule> rulesForDomain(Domain domain) {
    return rules.stream()
        .filter(CatalogRule::enabled)
        .filter(r -> r.domain() == domain)
        .toList();
  }

  /** Looks up a rule by ID. */
  public java.util.Optional<CatalogRule> findById(String ruleId) {
    return rules.stream().filter(r -> r.id().equals(ruleId)).findFirst();
  }

  /** Returns grouped rules by domain (for parallel dispatch). */
  public Map<Domain, List<CatalogRule>> groupedByDomain() {
    return rules.stream()
        .filter(CatalogRule::enabled)
        .collect(Collectors.groupingBy(CatalogRule::domain));
  }

  // ── Load + Validate ────────────────────────────────────────────────────────

  private List<CatalogRule> loadAndValidate() {
    try {
      byte[] catalogBytes = readResource(CATALOG_PATH);
      validateHash(catalogBytes);
      return parseCatalog(catalogBytes);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to load rules/catalog.json", e);
    }
  }

  private List<CatalogRule> parseCatalog(byte[] bytes) throws IOException {
    var rawList = objectMapper.readValue(bytes, new TypeReference<List<Map<String, Object>>>() {});
    return rawList.stream().map(this::mapEntry).toList();
  }

  @SuppressWarnings("unchecked")
  private CatalogRule mapEntry(Map<String, Object> m) {
    return new CatalogRule(
        (String)  m.get("id"),
        Domain.valueOf((String) m.get("domain")),
        Severity.valueOf((String) m.get("severity")),
        (String)  m.getOrDefault("description", ""),
        (boolean) m.getOrDefault("enabled", true),
        (String)  m.getOrDefault("version", "1.0"),
        m.containsKey("references")
            ? (List<String>) m.get("references")
            : List.of(),
        (String)  m.getOrDefault("sinceFramework", null),
        (String)  m.getOrDefault("sinceVersion",   null)
    );
  }

  /**
   * Validates the catalog SHA-256 hash against the stored checksum file.
   * If the hash file is absent (first run / development mode), logs a warning
   * and skips validation.
   */
  private void validateHash(byte[] catalogBytes) {
    String actualHash = sha256hex(catalogBytes);
    try {
      byte[] storedHashBytes = readResource(CATALOG_HASH_PATH);
      String storedHash = new String(storedHashBytes).strip();
      if (!actualHash.equals(storedHash)) {
        throw new CatalogIntegrityException(
            "catalog.json hash mismatch! " +
            "Expected: " + storedHash + " | Actual: " + actualHash +
            " — A rule was changed without a CHANGELOG entry. See Section 7A.5.");
      }
      log.debug("Catalog integrity validated (SHA-256: {})", actualHash);
    } catch (IOException e) {
      log.warn("catalog.sha256 not found — skipping hash guard (ok in development). " +
               "Current hash: {}", actualHash);
    }
  }

  private byte[] readResource(String path) throws IOException {
    var resource = new ClassPathResource(path);
    if (!resource.exists()) {
      // Fall back to filesystem root-relative path (for integration tests)
      Path fsPath = Path.of(path);
      if (Files.exists(fsPath)) {
        return Files.readAllBytes(fsPath);
      }
      throw new IOException("Resource not found: " + path);
    }
    try (InputStream in = resource.getInputStream()) {
      return in.readAllBytes();
    }
  }

  private String sha256hex(byte[] data) {
    try {
      var digest = MessageDigest.getInstance("SHA-256");
      return HexFormat.of().formatHex(digest.digest(data));
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("SHA-256 not available", e);
    }
  }

  // ── Exception ──────────────────────────────────────────────────────────────

  /** Thrown when the catalog's SHA-256 checksum does not match the stored hash. */
  public static class CatalogIntegrityException extends RuntimeException {
    public CatalogIntegrityException(String message) {
      super(message);
    }
  }
}
