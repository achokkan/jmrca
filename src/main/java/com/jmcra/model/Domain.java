package com.jmcra.model;

/**
 * The twelve review domains that JMCRA organises every rule under.
 * Each constant maps to the {@code DOM-NNN} Rule ID prefix.
 * <p>
 * Spec: Section 3 (Review Domain Taxonomy).
 */
public enum Domain {

  /** Security — OWASP Top-10, CVEs, injection, secret leakage, JWT. */
  SEC("Security"),

  /** Resilience — circuit breakers, bulkheads, retries, timeouts, fallbacks. */
  RES("Resilience"),

  /** Observability — distributed tracing, metric naming, structured logging. */
  OBS("Observability"),

  /** API Design — REST/gRPC contracts, versioning, pagination, error envelopes. */
  API("API Design"),

  /** Data Access — N+1 queries, transactions, connection pools, lazy-load misuse. */
  DAT("Data Access"),

  /** Messaging — consumer groups, DLQ, offset management, schema evolution. */
  MSG("Messaging"),

  /** Concurrency — thread safety, virtual-thread compat, reactive backpressure. */
  CON("Concurrency"),

  /** Configuration — externalised config, secrets, 12-factor compliance. */
  CFG("Configuration"),

  /** Test Quality — coverage, test pyramid, mutation testing, contract tests. */
  TST("Test Quality"),

  /** Dependencies — CVEs, outdated versions, license compliance, SBOM. */
  DEP("Dependencies"),

  /** Performance — blocking calls on reactive threads, caching, GC pressure. */
  PRF("Performance"),

  /** Design & Patterns — SOLID violations, God class, boundary leakage. */
  DES("Design & Patterns");

  private final String displayName;

  Domain(String displayName) {
    this.displayName = displayName;
  }

  public String displayName() {
    return displayName;
  }

  /** Rule ID prefix for this domain, e.g. "SEC" for {@link #SEC}. */
  public String prefix() {
    return name();
  }
}
