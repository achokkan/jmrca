package com.jmcra.pipeline;

import com.jmcra.annotations.PipelineContractTest;
import com.jmcra.annotations.PipelineStage;
import com.jmcra.annotations.SpecOracle;
import com.jmcra.config.JmcraProperties;
import com.jmcra.model.ScanRequest;
import com.jmcra.model.WebhookPayload;
import com.jmcra.pipeline.stage1.HmacValidator;
import com.jmcra.pipeline.stage1.IngestService;
import com.jmcra.model.ScanStarted;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

import static org.assertj.core.api.Assertions.*;

/**
 * Stage 1 — Ingest — Contract Tests.
 *
 * SPC-007: HMAC-invalid webhooks rejected with 401.
 *           Valid webhook produces ScanRequest with SHA, branch, delta file list.
 * SPC-008: ScanStarted event emitted after successful ingest.
 *
 * Per SDD Section 7A.7: these tests MUST be committed before the IngestService
 * implementation and MUST fail on a blank stub (RED state).
 *
 * Spec: Section 4.2 (Stage 1 — Ingest), Section 7A.3 (Pipeline Stage Contract Tests).
 */
@PipelineContractTest(specClause = "SPC-007,SPC-008", stage = PipelineStage.INGEST)
@ExtendWith(MockitoExtension.class)
class IngestStageContractTest {

  private static final String WEBHOOK_SECRET = "test-secret-do-not-use-in-production";

  private HmacValidator  hmacValidator;
  private IngestService  ingestService;
  private Sinks.Many<ScanStarted> eventBus;

  private JmcraProperties props;

  @BeforeEach
  void setUp() {
    props         = buildTestProperties();
    hmacValidator = new HmacValidator();
    eventBus      = Sinks.many().multicast().onBackpressureBuffer();
    ingestService = new IngestService(hmacValidator, props,
        new com.fasterxml.jackson.databind.ObjectMapper(), eventBus);
  }

  // ── SPC-007 — HMAC Rejection ───────────────────────────────────────────────

  @Test
  @SpecOracle("HMAC validation — invalid signature → 401 Unauthorized with body signature_mismatch")
  void rejectsInvalidHmacSignature() {
    var payload = new WebhookPayload(
        "github",
        validGitHubBody(),
        "sha256=000000000invalid",  // wrong signature
        "delivery-001"
    );

    StepVerifier.create(ingestService.ingest(payload))
        .expectErrorSatisfies(error -> {
          assertThat(error).isInstanceOf(ResponseStatusException.class);
          var ex = (ResponseStatusException) error;
          assertThat(ex.getStatusCode().value()).isEqualTo(401);
          assertThat(ex.getReason()).isEqualTo("signature_mismatch");
        })
        .verify();
  }

  @Test
  @SpecOracle("Missing X-Hub-Signature-256 header → 401")
  void rejectsMissingSignatureHeader() {
    var payload = new WebhookPayload("github", validGitHubBody(), null, "delivery-002");

    StepVerifier.create(ingestService.ingest(payload))
        .expectError(ResponseStatusException.class)
        .verify();
  }

  // ── SPC-007 — Valid Webhook → ScanRequest ──────────────────────────────────

  @Test
  @SpecOracle("Valid GitHub PR webhook → ScanRequest with 40-char commitSha, non-blank branch, scan profile")
  void producesScanRequestFromValidGitHubWebhook() {
    String body      = validGitHubBody();
    String signature = computeGitHubSignature(WEBHOOK_SECRET, body);
    var payload = new WebhookPayload("github", body, signature, "delivery-003");

    StepVerifier.create(ingestService.ingest(payload))
        .assertNext(req -> {
          assertThat(req.commitSha()).matches("[0-9a-f]{40}");
          assertThat(req.branch()).isNotBlank();
          assertThat(req.scanProfile()).isNotNull();
          assertThat(req.scanId()).isNotNull();
          assertThat(req.repositoryUrl()).isNotBlank();
        })
        .verifyComplete();
  }

  // ── SPC-008 — ScanStarted Event ────────────────────────────────────────────

  @Test
  @SpecOracle("ScanStarted event emitted on the internal event bus after successful ingest")
  void emitsScanStartedEventAfterSuccessfulIngest() {
    String body      = validGitHubBody();
    String signature = computeGitHubSignature(WEBHOOK_SECRET, body);
    var payload = new WebhookPayload("github", body, signature, "delivery-004");

    var eventFlux = eventBus.asFlux();

    StepVerifier.create(ingestService.ingest(payload))
        .assertNext(req -> assertThat(req).isNotNull())
        .verifyComplete();

    StepVerifier.create(eventFlux.next())
        .assertNext(event -> {
          assertThat(event).isNotNull();
          assertThat(event.scanRequest()).isNotNull();
          assertThat(event.scanId()).isNotNull();
        })
        .thenCancel()
        .verify();
  }

  // ── Helpers ────────────────────────────────────────────────────────────────

  private String validGitHubBody() {
    return """
        {
          "action": "opened",
          "pull_request": {
            "number": 42,
            "head": {
              "sha": "a1b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4e5f6a1b2",
              "ref": "feature/jmcra-test"
            }
          },
          "repository": {
            "clone_url": "https://github.com/acme/service.git"
          }
        }
        """;
  }

  private String computeGitHubSignature(String secret, String body) {
    try {
      var mac = Mac.getInstance("HmacSHA256");
      mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
      return "sha256=" + HexFormat.of().formatHex(
          mac.doFinal(body.getBytes(StandardCharsets.UTF_8)));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private JmcraProperties buildTestProperties() {
    return new JmcraProperties(
        new JmcraProperties.Ingest(WEBHOOK_SECRET, java.util.List.of("github", "gitlab")),
        new JmcraProperties.Pipeline(4, java.time.Duration.ofSeconds(120), false, null, null),
        new JmcraProperties.Llm(
            com.jmcra.llm.LlmProvider.CLAUDE,
            new JmcraProperties.Llm.Claude("test-key"),
            new JmcraProperties.Llm.OpenAi(""),
            new JmcraProperties.Llm.Gemini(""), 60),
        new JmcraProperties.Delivery(
            new JmcraProperties.Delivery.GitHub(false, "", "https://api.github.com"),
            new JmcraProperties.Delivery.Jira(false, null, null, null, null),
            new JmcraProperties.Delivery.Slack(false, null, 5),
            "NONE"),
        new JmcraProperties.Git("/tmp/jmcra-test", 1, true)
    );
  }
}
