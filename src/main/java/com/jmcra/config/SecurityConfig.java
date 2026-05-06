package com.jmcra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Spring Security WebFlux configuration.
 *
 * <h3>Strategy</h3>
 * JMCRA does not use session-based auth. All authentication is done at the
 * webhook-payload level via HMAC validation in {@code IngestService}.
 * Spring Security is configured to:
 * <ul>
 *   <li>Allow all {@code /webhook/**} requests (HMAC validated in service layer).</li>
 *   <li>Allow Actuator health/info endpoints without authentication.</li>
 *   <li>Require authentication for all other endpoints (future admin API).</li>
 *   <li>Disable CSRF (not applicable for stateless webhook receivers).</li>
 * </ul>
 *
 * Spec: Section 4.2 (Stage 1 — Ingest): "Must validate webhook HMAC signature before processing."
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    return http
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .authorizeExchange(exchanges -> exchanges
            // Web UI and Static Assets
            .pathMatchers("/", "/index.html", "/css/**", "/js/**", "/assets/**").permitAll()
            // Webhook endpoints: HMAC validated by IngestService
            .pathMatchers("/webhook/**").permitAll()
            // API endpoints for UI (permitted for now, basic-auth in production)
            .pathMatchers("/api/**").permitAll()
            // Spring Boot Actuator health probes
            .pathMatchers("/actuator/health", "/actuator/info").permitAll()
            // All other endpoints require authentication
            .anyExchange().authenticated()
        )
        .httpBasic(org.springframework.security.config.Customizer.withDefaults())
        .build();
  }
}
