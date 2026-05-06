package com.jmcra.pipeline.stage1;

import com.jmcra.model.WebhookPayload;
import com.jmcra.pipeline.PipelineOrchestrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Webhook endpoint — entry point for Stage 1 (Ingest).
 *
 * <h3>Endpoints</h3>
 * <ul>
 *   <li>{@code POST /webhook/github} — receives GitHub PR / push webhook events.</li>
 *   <li>{@code POST /webhook/gitlab} — receives GitLab MR / push webhook events.</li>
 *   <li>{@code POST /webhook/scan}   — manual/scheduled full-repo scan trigger.</li>
 * </ul>
 *
 * <h3>HMAC Validation</h3>
 * All payloads are validated by {@link IngestService} before processing.
 * Invalid signatures return {@code 401 Unauthorized} with body {@code {"error":"signature_mismatch"}}.
 * <p>
 * Spec: Section 2.3 (Integration Points — GitHub/GitLab webhook trigger).
 *       Section 4.2 (Stage 1 — Ingest). SPC-007.
 */
@RestController
@RequestMapping("/webhook")
public class WebhookController {

  private static final Logger log = LoggerFactory.getLogger(WebhookController.class);

  private final IngestService ingestService;
  private final PipelineOrchestrator orchestrator;

  public WebhookController(IngestService ingestService, PipelineOrchestrator orchestrator) {
    this.ingestService = ingestService;
    this.orchestrator  = orchestrator;
  }

  // ── GitHub ──────────────────────────────────────────────────────────────────

  /**
   * Receives a GitHub webhook event (PR opened/synchronised, push).
   *
   * @param signature  {@code X-Hub-Signature-256} header value.
   * @param deliveryId {@code X-GitHub-Delivery} header — used for idempotency.
   * @param event      {@code X-GitHub-Event} header (e.g., "pull_request").
   * @param body       Raw JSON body.
   */
  @PostMapping(
      value    = "/github",
      consumes = MediaType.APPLICATION_JSON_VALUE
  )
  public Mono<ResponseEntity<String>> handleGitHub(
      @RequestHeader(value = "X-Hub-Signature-256", required = false) String signature,
      @RequestHeader(value = "X-GitHub-Delivery",   required = false) String deliveryId,
      @RequestHeader(value = "X-GitHub-Event",      required = false) String event,
      @RequestBody String body
  ) {
    log.debug("GitHub webhook received: event={}, deliveryId={}", event, deliveryId);

    var payload = new WebhookPayload("github", body, signature,
        deliveryId != null ? deliveryId : "unknown");

    return ingestService.ingest(payload)
        .map(req -> {
          orchestrator.runFromRequest(req).subscribe(); // Background
          return ResponseEntity.status(HttpStatus.ACCEPTED)
              .body("{\"status\":\"ACCEPTED\",\"scanId\":\"" + req.scanId() + "\"}");
        })
        .onErrorResume(org.springframework.web.server.ResponseStatusException.class,
            e -> Mono.just(ResponseEntity.status(e.getStatusCode()).body(e.getMessage())));
  }

  // ── GitLab ──────────────────────────────────────────────────────────────────

  /**
   * Receives a GitLab webhook event (MR opened/updated, push).
   *
   * @param token     {@code X-Gitlab-Token} secret token header.
   * @param eventType {@code X-Gitlab-Event} header (e.g., "Merge Request Hook").
   * @param body      Raw JSON body.
   */
  @PostMapping(
      value    = "/gitlab",
      consumes = MediaType.APPLICATION_JSON_VALUE
  )
  public Mono<ResponseEntity<String>> handleGitLab(
      @RequestHeader(value = "X-Gitlab-Token", required = false) String token,
      @RequestHeader(value = "X-Gitlab-Event", required = false) String eventType,
      @RequestBody String body
  ) {
    log.debug("GitLab webhook received: event={}", eventType);

    var payload = new WebhookPayload("gitlab", body, token, "gitlab-" + System.nanoTime());

    return ingestService.ingest(payload)
        .map(req -> {
          orchestrator.runFromRequest(req).subscribe(); // Background
          return ResponseEntity.status(HttpStatus.ACCEPTED)
              .body("{\"status\":\"ACCEPTED\",\"scanId\":\"" + req.scanId() + "\"}");
        })
        .onErrorResume(org.springframework.web.server.ResponseStatusException.class,
            e -> Mono.just(ResponseEntity.status(e.getStatusCode()).body(e.getMessage())));
  }

  // ── Local ───────────────────────────────────────────────────────────────────

  /**
   * Triggers a scan of a local directory.
   *
   * @param body JSON body containing "path" and optionally "branch" and "profile".
   */
  @PostMapping(
      value    = "/local",
      consumes = MediaType.APPLICATION_JSON_VALUE
  )
  public Mono<ResponseEntity<String>> handleLocal(@RequestBody String body) {
    log.info("Local scan trigger received");

    var payload = new WebhookPayload("local", body, "no-sig",
        "local-" + System.currentTimeMillis());

    return ingestService.ingest(payload)
        .map(req -> {
          orchestrator.runFromRequest(req).subscribe(); // Background
          return ResponseEntity.status(HttpStatus.ACCEPTED)
              .body("{\"status\":\"ACCEPTED\",\"scanId\":\"" + req.scanId() + "\"}");
        })
        .onErrorResume(Exception.class,
            e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\":\"" + e.getMessage() + "\"}")));
  }

  // ── Health ──────────────────────────────────────────────────────────────────

  @GetMapping("/health")
  public Mono<ResponseEntity<String>> health() {
    return Mono.just(ResponseEntity.ok("{\"status\":\"UP\",\"agent\":\"JMCRA\",\"version\":\"1.0\"}"));
  }
}
