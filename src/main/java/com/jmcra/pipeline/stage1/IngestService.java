package com.jmcra.pipeline.stage1;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmcra.config.JmcraProperties;
import com.jmcra.model.ScanMode;
import com.jmcra.model.ScanProfile;
import com.jmcra.model.ScanRequest;
import com.jmcra.model.ScanStarted;
import com.jmcra.model.WebhookPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.List;

/**
 * Stage 1 — Ingest.
 *
 * <h3>Responsibilities</h3>
 * <ol>
 *   <li>Validates the webhook HMAC signature (rejects with 401 on mismatch).</li>
 *   <li>Normalises the raw GitHub / GitLab webhook payload into a {@link ScanRequest}.</li>
 *   <li>Emits a {@link ScanStarted} event on the internal reactive event bus.</li>
 * </ol>
 *
 * <h3>Supported Modes</h3>
 * <ul>
 *   <li><strong>PR webhook</strong>: diff-only scan of changed files.</li>
 *   <li><strong>Scheduled</strong>: full repository scan.</li>
 *   <li><strong>CLI</strong>: full scan triggered directly.</li>
 * </ul>
 *
 * Spec: Section 4.2 (Stage 1 — Ingest). SPC-007.
 */
@Service
public class IngestService {

  private static final Logger log = LoggerFactory.getLogger(IngestService.class);

  private final HmacValidator    hmacValidator;
  private final JmcraProperties  props;
  private final ObjectMapper     mapper;
  private final Sinks.Many<ScanStarted> eventBus;

  public IngestService(HmacValidator hmacValidator,
                       JmcraProperties props,
                       ObjectMapper mapper,
                       Sinks.Many<ScanStarted> eventBus) {
    this.hmacValidator = hmacValidator;
    this.props         = props;
    this.mapper        = mapper;
    this.eventBus      = eventBus;
  }

  // ── Public API ──────────────────────────────────────────────────────────────

  /**
   * Processes an incoming webhook payload.
   * Validates HMAC, normalises the payload into a {@link ScanRequest},
   * and emits a {@link ScanStarted} event.
   *
   * @param payload The raw webhook payload (body + signature header).
   * @return A cold {@code Mono} emitting the normalised {@link ScanRequest}.
   * @throws ResponseStatusException 401 if HMAC validation fails (SPC-007).
   */
  public Mono<ScanRequest> ingest(WebhookPayload payload) {
    return Mono.fromCallable(() -> validate(payload))
        .flatMap(this::buildScanRequest)
        .doOnSuccess(req -> {
          log.info("[{}] Scan started — repo={}, branch={}, mode={}",
              req.scanId(), req.repositoryUrl(), req.branch(), req.scanMode());
          eventBus.tryEmitNext(new ScanStarted(req));
        })
        .doOnError(e -> log.error("Ingest failed: {}", e.getMessage()));
  }

  // ── HMAC Validation ────────────────────────────────────────────────────────

  /**
   * Validates the webhook signature. Returns the payload if valid.
   * Throws {@link ResponseStatusException} with 401 if invalid.
   */
  private WebhookPayload validate(WebhookPayload payload) {
    String secret = props.ingest().webhookSecret();
    boolean valid = switch (payload.source().toLowerCase()) {
      case "github" -> hmacValidator.isValidGitHubSignature(
          secret, payload.rawBody(), payload.signatureHeader());
      case "gitlab" -> hmacValidator.isValidGitLabToken(
          secret, payload.signatureHeader());
      case "local"  -> true; // Internally trusted trigger
      default -> {
        log.warn("Unknown webhook source: {}", payload.source());
        yield false;
      }
    };

    if (!valid) {
      throw new ResponseStatusException(
          HttpStatus.UNAUTHORIZED, "signature_mismatch");
    }
    return payload;
  }

  // ── ScanRequest Construction ───────────────────────────────────────────────

  private Mono<ScanRequest> buildScanRequest(WebhookPayload payload) {
    return Mono.fromCallable(() -> {
      JsonNode root = mapper.readTree(payload.rawBody());
      return switch (payload.source().toLowerCase()) {
        case "github" -> buildFromGitHub(root, payload.deliveryId());
        case "gitlab" -> buildFromGitLab(root, payload.deliveryId());
        case "local"  -> buildFromLocal(root, payload.deliveryId());
        default -> throw new IllegalArgumentException(
            "Unsupported webhook source: " + payload.source());
      };
    });
  }

  private ScanRequest buildFromGitHub(JsonNode root, String deliveryId) {
    // Determine PR number and changed files
    String prNumber = root.path("pull_request").path("number").asText(null);
    boolean isPr    = prNumber != null;

    List<String> changedFiles = new ArrayList<>();
    if (isPr && root.has("pull_request")) {
      // Changed files come from a separate GitHub API call in Stage 2
      // We record the PR number so Stage 2 can fetch the file list
    }

    String commitSha = isPr
        ? root.at("/pull_request/head/sha").asText()
        : root.at("/after").asText();

    String repoUrl = root.at("/repository/clone_url").asText();
    String branch  = isPr
        ? root.at("/pull_request/head/ref").asText()
        : root.at("/ref").asText().replaceFirst("refs/heads/", "");

    return ScanRequest.builder()
        .repositoryUrl(repoUrl)
        .commitSha(commitSha.length() == 40 ? commitSha : "a".repeat(40))
        .branch(branch)
        .changedFiles(changedFiles)
        .scanProfile(ScanProfile.defaultProfile())
        .scanMode(isPr ? ScanMode.DIFF_ONLY : ScanMode.FULL)
        .source("github-pr")
        .pullRequestId(prNumber)
        .build();
  }

  private ScanRequest buildFromGitLab(JsonNode root, String deliveryId) {
    String mrIid    = root.path("object_attributes").path("iid").asText(null);
    boolean isMr    = mrIid != null;
    String commitSha = root.at("/object_attributes/last_commit/id").asText(
        root.at("/checkout_sha").asText());
    String repoUrl  = root.at("/project/http_url").asText();
    String branch   = root.at("/object_attributes/source_branch").asText(
        root.at("/ref").asText().replaceFirst("refs/heads/", ""));

    return ScanRequest.builder()
        .repositoryUrl(repoUrl)
        .commitSha(commitSha.length() == 40 ? commitSha : "a".repeat(40))
        .branch(branch)
        .changedFiles(List.of())
        .scanProfile(ScanProfile.defaultProfile())
        .scanMode(isMr ? ScanMode.DIFF_ONLY : ScanMode.FULL)
        .source("gitlab-mr")
        .pullRequestId(mrIid)
        .build();
  }
  private ScanRequest buildFromLocal(JsonNode root, String deliveryId) {
    String path    = root.path("path").asText();
    String branch  = root.path("branch").asText("local");
    String profile = root.path("profile").asText("DEFAULT");

    if (path == null || path.isBlank()) {
      throw new IllegalArgumentException("Local scan requires 'path' property");
    }

    return ScanRequest.builder()
        .repositoryUrl("local://" + path)
        .commitSha("0".repeat(40)) // Dummy SHA for local scans
        .branch(branch)
        .changedFiles(List.of())
        .scanProfile(ScanProfile.defaultProfile())
        .scanMode(ScanMode.FULL)
        .source("cli-local")
        .build();
  }
}
