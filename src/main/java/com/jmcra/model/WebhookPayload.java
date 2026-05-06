package com.jmcra.model;

/**
 * Raw webhook payload envelope received by the {@code WebhookController}.
 * This is the first object constructed during Stage 1 (Ingest) — it holds
 * the raw HTTP body and the signature header before HMAC validation.
 * <p>
 * Spec: Section 4.2 (Stage 1 — Ingest): "Must validate webhook HMAC signature before processing."
 *
 * @param source         Identifies the origin system: "github" or "gitlab".
 * @param rawBody        Raw JSON body of the webhook HTTP request (as received).
 * @param signatureHeader The value of the HMAC signature header:
 *                       {@code X-Hub-Signature-256} (GitHub) or
 *                       {@code X-Gitlab-Token} (GitLab).
 * @param deliveryId     Unique delivery ID from the SCM system (for idempotency).
 */
public record WebhookPayload(
    String source,
    String rawBody,
    String signatureHeader,
    String deliveryId
) {

  public boolean isGitHub() {
    return "github".equalsIgnoreCase(source);
  }

  public boolean isGitLab() {
    return "gitlab".equalsIgnoreCase(source);
  }
}
