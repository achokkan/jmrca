package com.jmcra.model;

import java.util.List;
import java.util.Objects;

/**
 * A named rule-set profile that controls which domains and rules are active
 * during a scan. Referenced from {@link ScanRequest}.
 * <p>
 * The {@code default} profile activates all rules in the catalog.
 * Teams can define custom profiles in {@code .jmcra/suppressions.yml}.
 * <p>
 * Spec: Section 4.2 (Stage 1 — Ingest), Section 4.2 (Stage 3 — Analysis Dispatch).
 *
 * @param name             Human-readable profile name (e.g., "default", "security-only").
 * @param enabledDomains   Domains active in this profile; empty list means all domains.
 * @param disabledRuleIds  Specific rule IDs suppressed by this profile.
 */
public record ScanProfile(
    String name,
    List<Domain> enabledDomains,
    List<String> disabledRuleIds
) {

  public ScanProfile {
    Objects.requireNonNull(name, "profile name must not be null");
    enabledDomains  = enabledDomains  != null ? List.copyOf(enabledDomains)  : List.of();
    disabledRuleIds = disabledRuleIds != null ? List.copyOf(disabledRuleIds) : List.of();
  }

  /** Convenience factory: activate everything. */
  public static ScanProfile defaultProfile() {
    return new ScanProfile("default", List.of(), List.of());
  }

  /** Returns {@code true} if this profile enables the given domain. */
  public boolean isDomainEnabled(Domain domain) {
    return enabledDomains.isEmpty() || enabledDomains.contains(domain);
  }

  /** Returns {@code true} if the given rule ID is suppressed by this profile. */
  public boolean isRuleDisabled(String ruleId) {
    return disabledRuleIds.contains(ruleId);
  }
}
