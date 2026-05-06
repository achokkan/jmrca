package com.jmcra.model;

/**
 * A single Maven or Gradle dependency resolved from the project's build file.
 * Used by DEP domain rules (e.g., DEP-001 CVE detection, DEP-005 javax.inject migration).
 * <p>
 * Spec: Section 3 (DEP domain), Section 5 (Rule Catalog — DEP-001, DEP-005).
 *
 * @param groupId    Maven groupId.
 * @param artifactId Maven artifactId.
 * @param version    Resolved version string.
 * @param scope      Maven scope (compile, test, provided, runtime) or Gradle configuration name.
 * @param direct     {@code true} if this is a direct dependency; {@code false} if transitive.
 */
public record Dependency(
    String groupId,
    String artifactId,
    String version,
    String scope,
    boolean direct
) {

  /** Maven coordinate in {@code groupId:artifactId:version} format. */
  public String coordinate() {
    return "%s:%s:%s".formatted(groupId, artifactId, version);
  }

  /** Convenience factory for a direct compile-scope dependency. */
  public static Dependency direct(String groupId, String artifactId, String version) {
    return new Dependency(groupId, artifactId, version, "compile", true);
  }

  /** Convenience factory for a transitive dependency. */
  public static Dependency transitive(String groupId, String artifactId, String version, String scope) {
    return new Dependency(groupId, artifactId, version, scope, false);
  }
}
