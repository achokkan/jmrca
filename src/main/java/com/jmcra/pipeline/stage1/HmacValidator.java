package com.jmcra.pipeline.stage1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * HMAC-SHA256 webhook signature validator.
 *
 * <h3>GitHub</h3>
 * Validates the {@code X-Hub-Signature-256} header using the configured webhook secret.
 * Expected format: {@code sha256=<hex-digest>}.
 *
 * <h3>GitLab</h3>
 * Validates the {@code X-Gitlab-Token} header as a simple secret token comparison.
 *
 * <h3>Security</h3>
 * Uses {@link MessageDigest#isEqual} for constant-time comparison to prevent
 * timing oracle attacks.
 * <p>
 * Spec: Section 4.2 (Stage 1 — Ingest):
 * "Must validate webhook HMAC signature before processing."
 * SPC-007: "HMAC-invalid webhooks rejected with 401."
 */
@Component
public class HmacValidator {

  private static final Logger log = LoggerFactory.getLogger(HmacValidator.class);
  private static final String HMAC_ALGORITHM = "HmacSHA256";
  private static final String GITHUB_PREFIX  = "sha256=";

  /**
   * Validates a GitHub webhook HMAC-SHA256 signature.
   *
   * @param secret         The configured webhook secret.
   * @param rawBody        The raw request body bytes, exactly as received.
   * @param signatureHeader The value of the {@code X-Hub-Signature-256} header.
   * @return {@code true} if the signature is valid.
   */
  public boolean isValidGitHubSignature(String secret, String rawBody, String signatureHeader) {
    if (secret == null || secret.isBlank()) {
      log.warn("Webhook secret is not configured — rejecting all requests");
      return false;
    }
    if (signatureHeader == null || !signatureHeader.startsWith(GITHUB_PREFIX)) {
      log.warn("Missing or malformed X-Hub-Signature-256 header");
      return false;
    }

    try {
      String expected = GITHUB_PREFIX + computeHmac(secret, rawBody);
      byte[] a = expected.getBytes(StandardCharsets.UTF_8);
      byte[] b = signatureHeader.getBytes(StandardCharsets.UTF_8);
      boolean valid = MessageDigest.isEqual(a, b);
      if (!valid) {
        log.warn("GitHub HMAC signature mismatch — request rejected");
      }
      return valid;
    } catch (Exception e) {
      log.error("HMAC validation error: {}", e.getMessage());
      return false;
    }
  }

  /**
   * Validates a GitLab webhook token (simple secret token comparison).
   *
   * @param secret      The configured webhook secret.
   * @param tokenHeader The value of the {@code X-Gitlab-Token} header.
   * @return {@code true} if the token matches.
   */
  public boolean isValidGitLabToken(String secret, String tokenHeader) {
    if (secret == null || secret.isBlank() || tokenHeader == null) return false;
    byte[] a = secret.getBytes(StandardCharsets.UTF_8);
    byte[] b = tokenHeader.getBytes(StandardCharsets.UTF_8);
    return MessageDigest.isEqual(a, b);
  }

  // ── Private Helpers ────────────────────────────────────────────────────────

  private String computeHmac(String secret, String body)
      throws NoSuchAlgorithmException, InvalidKeyException {
    var mac = Mac.getInstance(HMAC_ALGORITHM);
    mac.init(new SecretKeySpec(
        secret.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM));
    byte[] hash = mac.doFinal(body.getBytes(StandardCharsets.UTF_8));
    return HexFormat.of().formatHex(hash);
  }
}
