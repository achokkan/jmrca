package com.jmcra.pipeline.stage6;

import com.jmcra.config.JmcraProperties;
import com.jmcra.model.DeliveryResult;
import com.jmcra.model.FindingsReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * Delivers a summary card to Slack via Incoming Webhook.
 * Shows the health score, top-N findings, and a delta from the previous scan.
 * <p>
 * Spec: Section 4.2 (Stage 6): "Slack: summary card with top 5 findings and health score delta."
 */
@Component
public class SlackDelivery implements DeliveryTarget {

  private static final Logger log = LoggerFactory.getLogger(SlackDelivery.class);

  private final WebClient       webClient;
  private final JmcraProperties props;

  public SlackDelivery(WebClient.Builder webClientBuilder, JmcraProperties props) {
    this.props = props;
    this.webClient = webClientBuilder.build();
  }

  @Override public String  name()      { return "slack"; }
  @Override public boolean isEnabled() { return props.delivery().slack().enabled(); }

  @Override
  public Mono<DeliveryResult> deliver(FindingsReport report) {
    String webhookUrl = props.delivery().slack().webhookUrl();
    if (webhookUrl == null || webhookUrl.isBlank()) {
      return Mono.just(DeliveryResult.failure(name(), "Slack webhook URL not configured"));
    }

    int topN     = props.delivery().slack().topFindingsCount();
    var payload  = buildPayload(report, topN);

    return webClient.post()
        .uri(webhookUrl)
        .bodyValue(payload)
        .retrieve()
        .bodyToMono(String.class)
        .map(_ -> DeliveryResult.success(name(), "Slack notification sent", null))
        .onErrorResume(e -> {
          log.error("[{}] Slack delivery failed: {}", report.scanId(), e.getMessage());
          return Mono.just(DeliveryResult.failure(name(), e.getMessage()));
        });
  }

  // ── Block Kit Payload ──────────────────────────────────────────────────────

  private Map<String, Object> buildPayload(FindingsReport report, int topN) {
    var hs    = report.healthScore();
    String emoji = hs.score() >= 90 ? "✅"
        : hs.score() >= 70 ? "⚠️"
        : "🚨";

    var headerBlock = Map.of(
        "type", "header",
        "text", Map.of("type", "plain_text",
            "text", "%s JMCRA: %s (%.0f/100)".formatted(emoji, hs.label(), hs.score()))
    );

    var contextBlock = Map.of(
        "type", "context",
        "elements", List.of(
            Map.of("type", "mrkdwn",
                "text", "*Repo:* %s | *Branch:* %s | *Mode:* %s | *Duration:* %ds"
                    .formatted(report.repositoryUrl(), report.branch(),
                        report.scanMode(), report.scanDuration().toSeconds()))
        )
    );

    var statsBlock = Map.of(
        "type", "section",
        "fields", List.of(
            Map.of("type", "mrkdwn", "text",
                "*🔴 Critical:* %d".formatted(hs.criticalCount())),
            Map.of("type", "mrkdwn", "text",
                "*🟠 High:* %d".formatted(hs.highCount())),
            Map.of("type", "mrkdwn", "text",
                "*🟡 Medium:* %d".formatted(hs.mediumCount())),
            Map.of("type", "mrkdwn", "text",
                "*🔵 Low:* %d".formatted(hs.lowCount()))
        )
    );

    var divider = Map.of("type", "divider");

    var topFindingBlocks = report.topFindings(topN).stream()
        .map(f -> Map.<String, Object>of(
            "type", "section",
            "text", Map.of("type", "mrkdwn",
                "text", "*[%s]* %s\n`%s:%d` — _%s_".formatted(
                    f.ruleId(), f.title(), f.file(), f.line(), f.severity()))
        ))
        .toList();

    var blocks = new java.util.ArrayList<>(List.of(headerBlock, contextBlock, statsBlock, divider));
    blocks.addAll(topFindingBlocks);

    return Map.of("blocks", blocks);
  }
}
