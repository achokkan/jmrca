package com.jmcra;

import com.jmcra.config.JmcraProperties;
import com.jmcra.model.ScanStarted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Sinks;

@SpringBootApplication
@EnableConfigurationProperties(JmcraProperties.class)
public class JmcraApplication {

  private static final Logger log = LoggerFactory.getLogger(JmcraApplication.class);

  public static void main(String[] args) {
    var app = new SpringApplication(JmcraApplication.class);
    app.run(args);
    log.info("""
        ╔══════════════════════════════════════════════════════╗
        ║     JMCRA — Java Microservices Code Review Agent     ║
        ║     Version 1.0.0  |  SDD Blueprint v1.2            ║
        ║     Target: Java 25 LTS / Spring Boot 4.0           ║
        ╚══════════════════════════════════════════════════════╝
        """);
  }

  /**
   * Provides the primary ObjectMapper for the application.
   * Configured to find and register all available modules (like JavaTimeModule).
   */
  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper().findAndRegisterModules();
  }

  /**
   * Reactive internal event bus for {@link ScanStarted} events.
   * Uses a multicast sink so multiple subscribers can listen to scan events
   * (e.g., metrics recorder, audit logger).
   * <p>
   * Spec: Section 4.2 (Stage 1 — Ingest): "Emits ScanStarted event on the internal bus."
   */
  @Bean
  public Sinks.Many<ScanStarted> scanEventBus() {
    return Sinks.many().multicast().onBackpressureBuffer();
  }
}
