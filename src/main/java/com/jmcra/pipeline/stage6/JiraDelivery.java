package com.jmcra.pipeline.stage6;

import com.jmcra.config.JmcraProperties;
import com.jmcra.model.DeliveryResult;
import com.jmcra.model.Finding;
import com.jmcra.model.FindingsReport;
import com.jmcra.model.Severity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * Delivers CRITICAL and HIGH findings to JIRA as sub-tasks linked to the PR.
 * Only P0 (CRITICAL) and P1 (HIGH) findings create JIRA issues, as specified in SDD Section 4.2.
 * <p>
 * Spec: Section 4.2 (Stage 6): "JIRA: creates sub-tasks for P0/P1 findings, links to PR."
 */
@Component
public class JiraDelivery implements DeliveryTarget {

  private static final Logger log = LoggerFactory.getLogger(JiraDelivery.class);

  private final WebClient       webClient;
  private final JmcraProperties props;

  public JiraDelivery(WebClient.Builder webClientBuilder, JmcraProperties props) {
    this.props = props;
    var jira   = props.delivery().jira();

    if (jira.baseUrl() != null && !jira.baseUrl().isBlank()) {
      String credentials = java.util.Base64.getEncoder().encodeToString(
          (jira.username() + ":" + jira.apiToken()).getBytes());
      this.webClient = webClientBuilder
          .baseUrl(jira.baseUrl())
          .defaultHeader("Authorization", "Basic " + credentials)
          .defaultHeader("Content-Type",  "application/json")
          .build();
    } else {
      this.webClient = webClientBuilder.build();
    }
  }

  @Override public String  name()      { return "jira"; }
  @Override public boolean isEnabled() { return props.delivery().jira().enabled(); }

  @Override
  public Mono<DeliveryResult> deliver(FindingsReport report) {
    var priorityFindings = report.priorityFindings();
    if (priorityFindings.isEmpty()) {
      return Mono.just(DeliveryResult.success(name(), "No P0/P1 findings — no tickets created", null));
    }

    return createIssues(priorityFindings, report)
        .map(keys -> DeliveryResult.success(name(),
            "Created %d JIRA issues: %s".formatted(keys.size(), keys), String.join(",", keys)))
        .onErrorResume(e -> {
          log.error("[{}] JIRA delivery failed: {}", report.scanId(), e.getMessage());
          return Mono.just(DeliveryResult.failure(name(), e.getMessage()));
        });
  }

  private Mono<List<String>> createIssues(List<Finding> findings, FindingsReport report) {
    var projectKey = props.delivery().jira().projectKey();
    return reactor.core.publisher.Flux.fromIterable(findings)
        .flatMap(f -> createIssue(f, report, projectKey))
        .collectList();
  }

  private Mono<String> createIssue(Finding f, FindingsReport report, String projectKey) {
    String priority = f.severity() == Severity.CRITICAL ? "Highest" : "High";
    var body = Map.of(
        "fields", Map.of(
            "project",     Map.of("key", projectKey),
            "summary",     "[JMCRA][%s] %s".formatted(f.ruleId(), f.title()),
            "description", Map.of(
                "type",    "doc",
                "version", 1,
                "content", List.of(Map.of(
                    "type",    "paragraph",
                    "content", List.of(Map.of("type", "text", "text",
                        "File: %s:%d\n\n%s\n\nRemediation: %s\n\nRefs: %s".formatted(
                            f.file(), f.line(), f.message(), f.remediation(),
                            String.join(", ", f.references()))
                    ))
                ))
            ),
            "issuetype", Map.of("name", "Bug"),
            "priority",  Map.of("name", priority),
            "labels",    List.of("jmcra", f.ruleId().toLowerCase(), "automated")
        )
    );

    return webClient.post()
        .uri("/rest/api/3/issue")
        .bodyValue(body)
        .retrieve()
        .bodyToMono(com.fasterxml.jackson.databind.JsonNode.class)
        .map(node -> node.path("key").asText("UNKNOWN"))
        .doOnSuccess(key -> log.info("[{}] JIRA issue created: {} for {}",
            report.scanId(), key, f.ruleId()));
  }
}
