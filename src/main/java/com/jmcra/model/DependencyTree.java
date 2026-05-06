package com.jmcra.model;

import java.util.List;
import java.util.Objects;

/**
 * The full dependency graph for a scanned project.
 * Produced by {@code ParseIndexService} (Stage 2) from {@code pom.xml} or {@code build.gradle}.
 * Consumed by DEP domain rule evaluators.
 * <p>
 * Spec: Section 4.2 (Stage 2 — Parse &amp; Index), Section 3 (DEP domain).
 *
 * @param buildTool           The build tool detected: "maven" or "gradle".
 * @param directDependencies  Dependencies declared directly in the build file.
 * @param allDependencies     All dependencies including transitives (flattened).
 */
public record DependencyTree(
    String buildTool,
    List<Dependency> directDependencies,
    List<Dependency> allDependencies
) {

  public DependencyTree {
    Objects.requireNonNull(buildTool, "buildTool must not be null");
    directDependencies = directDependencies != null ? List.copyOf(directDependencies) : List.of();
    allDependencies    = allDependencies    != null ? List.copyOf(allDependencies)    : List.of();
  }

  /** Returns all dependencies (direct + transitive) that match the given groupId. */
  public List<Dependency> findByGroup(String groupId) {
    return allDependencies.stream()
        .filter(d -> d.groupId().equals(groupId))
        .toList();
  }

  /** Returns all dependencies matching the given Maven coordinate prefix. */
  public List<Dependency> findByCoordinate(String groupId, String artifactId) {
    return allDependencies.stream()
        .filter(d -> d.groupId().equals(groupId) && d.artifactId().equals(artifactId))
        .toList();
  }

  /** Returns {@code true} if any dependency (direct or transitive) matches the given groupId. */
  public boolean containsGroup(String groupId) {
    return allDependencies.stream().anyMatch(d -> d.groupId().equals(groupId));
  }

  public static DependencyTree empty(String buildTool) {
    return new DependencyTree(buildTool, List.of(), List.of());
  }
}
