package com.jmcra.rules.catalog;

import com.jmcra.model.Domain;
import com.jmcra.model.Severity;

/**
 * A single entry in the rule catalog ({@code rules/catalog.json}).
 * Loaded by {@link RuleCatalogLoader} at startup.
 *
 * @param id          Stable rule identifier (e.g., "SEC-001").
 * @param domain      Review domain (e.g., SEC, RES).
 * @param severity    Default severity.
 * @param description Human-readable description.
 * @param enabled     Whether the rule is active in the default profile.
 * @param version     Rule version (semver string, e.g., "1.3").
 * @param references  List of external reference strings (CWE, OWASP, etc.).
 * @param sinceFramework Framework name for version-gated rules (nullable).
 * @param sinceVersion   Minimum framework version string (nullable).
 */
public record CatalogRule(
    String   id,
    Domain   domain,
    Severity severity,
    String   description,
    boolean  enabled,
    String   version,
    java.util.List<String> references,
    String   sinceFramework,
    String   sinceVersion
) {

  /** Returns {@code true} if this rule has a version gate configured. */
  public boolean isVersionGated() {
    return sinceFramework != null && !sinceFramework.isBlank()
        && sinceVersion  != null && !sinceVersion.isBlank();
  }

  /** Short display label: "SEC-001 [CRITICAL]". */
  public String label() {
    return "%s [%s]".formatted(id, severity.name());
  }
}
