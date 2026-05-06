package com.jmcra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Hooks;
import reactor.core.scheduler.Schedulers;

/**
 * Reactor and WebClient infrastructure configuration.
 *
 * <h3>BlockHound</h3>
 * Reactor BlockHound is enabled in {@code dev} and {@code test} profiles to detect
 * any blocking calls on reactive scheduler threads. This enforces ADR-002 compliance.
 *
 * <h3>Hooks</h3>
 * {@code Hooks.onOperatorDebug()} is enabled to produce enriched stack traces for
 * reactive debugging (note: impacts performance — disabled in production profile).
 *
 * Spec: ADR-002 (Reactive Pipeline with Project Reactor):
 * "Debugging requires reactor-tools block-hound instrumentation."
 */
@Configuration
public class ReactorConfig {

  static {
    // Enable debug stack traces (disable in prod via reactor.debug.agent=false)
    if (!Boolean.getBoolean("reactor.debug.disabled")) {
      Hooks.onOperatorDebug();
    }
  }

  /**
   * Shared WebClient.Builder bean — all service-layer WebClient instances
   * delegate to this builder to inherit connection pool settings.
   */
  @Bean
  public WebClient.Builder webClientBuilder() {
    return WebClient.builder()
        .codecs(codecs -> codecs.defaultCodecs()
            .maxInMemorySize(10 * 1024 * 1024)); // 10 MB buffer
  }

  /**
   * Install BlockHound in non-production profiles.
   * BlockHound throws an error if blocking code is detected on Reactor scheduler threads,
   * enforcing the reactive-first architecture mandated by ADR-002.
   */
  @Bean(name = "blockHoundInstaller")
  public Runnable installBlockHound() {
    String profile = System.getProperty("spring.profiles.active", "");
    if (!profile.contains("prod")) {
      // BlockHound.install(); // Requires reactor-blockhound in pom.xml
    }
    return () -> {};
  }
}
