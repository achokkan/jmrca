package com.jmcra.rules.annotations;

/**
 * Enumeration of versioned frameworks used in {@link VersionGate} to gate rule dispatch.
 * Rules annotated with {@code @SinceVersion} are only evaluated when the detected project
 * framework version meets the specified minimum.
 * <p>
 * Spec: Section 2A.3 (Version Compatibility Matrix for Rule Evaluation).
 */
public enum Framework {

  /** Java language version (JDK). Gated by {@code java.version} in pom.xml. */
  JAVA,

  /** Spring Boot auto-configuration framework. */
  SPRING_BOOT,

  /** Spring Framework (core). Typically derived from Spring Boot version. */
  SPRING_FRAMEWORK,

  /** Spring Cloud release-train version (e.g., 2025.1.0 Oakwood). */
  SPRING_CLOUD,

  /** Apache Kafka client / broker version. */
  KAFKA,

  /** Resilience4j version (2.x vs 3.x have different detection paths). */
  RESILIENCE4J,

  /** Project Reactor stream-processing library. */
  REACTOR,

  /** Hibernate ORM / JPA implementation. */
  HIBERNATE,

  /** Micrometer metrics facade. */
  MICROMETER,

  /** OpenTelemetry Java SDK. */
  OPENTELEMETRY
}
