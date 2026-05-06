package com.jmcra.model;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * The normalised scan request produced by Stage 1 (Ingest).
 * This is the contract between the Ingest stage and all downstream stages.
 * <p>
 * Spec: Section 4.2 (Stage 1 — Ingest).
 *
 * @param scanId        Unique scan identifier (UUID).
 * @param repositoryUrl Remote Git URL of the repository being scanned.
 * @param commitSha     Full 40-character commit SHA being analysed.
 * @param branch        Branch name (e.g., "feature/jmcra-001").
 * @param changedFiles  Files changed in the PR diff (empty = full scan).
 * @param scanProfile   Active rule profile for this scan.
 * @param scanMode      Whether to scan all files or only the PR diff.
 * @param requestedAt   Timestamp when the scan was initiated.
 * @param source        Origin of the scan ("github-pr", "gitlab-mr", "scheduled", "cli").
 * @param pullRequestId PR/MR number, or {@code null} for non-PR scans.
 */
public record ScanRequest(
    String scanId,
    String repositoryUrl,
    String commitSha,
    String branch,
    List<String> changedFiles,
    ScanProfile scanProfile,
    ScanMode scanMode,
    Instant requestedAt,
    String source,
    String pullRequestId
) {

  public ScanRequest {
    Objects.requireNonNull(scanId,        "scanId must not be null");
    Objects.requireNonNull(repositoryUrl, "repositoryUrl must not be null");
    Objects.requireNonNull(branch,        "branch must not be null");
    Objects.requireNonNull(scanProfile,   "scanProfile must not be null");
    Objects.requireNonNull(scanMode,      "scanMode must not be null");
    Objects.requireNonNull(requestedAt,   "requestedAt must not be null");

    if (commitSha != null && !commitSha.matches("[0-9a-f]{40}"))
      throw new IllegalArgumentException("commitSha must be a 40-char hex SHA (was: " + commitSha + ")");

    changedFiles = changedFiles != null ? List.copyOf(changedFiles) : List.of();
  }

  /** Returns {@code true} if this scan targets only PR-changed files. */
  public boolean isDiffOnly() {
    return scanMode == ScanMode.DIFF_ONLY;
  }

  /** Returns {@code true} if this scan has a PR/MR number associated. */
  public boolean isPullRequest() {
    return pullRequestId != null && !pullRequestId.isBlank();
  }

  /** Convenience builder. */
  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private String      scanId        = UUID.randomUUID().toString();
    private String      repositoryUrl;
    private String      commitSha;
    private String      branch;
    private List<String> changedFiles = List.of();
    private ScanProfile scanProfile   = ScanProfile.defaultProfile();
    private ScanMode    scanMode      = ScanMode.DIFF_ONLY;
    private Instant     requestedAt   = Instant.now();
    private String      source        = "unknown";
    private String      pullRequestId;

    public Builder repositoryUrl(String v)     { repositoryUrl = v;  return this; }
    public Builder commitSha(String v)         { commitSha     = v;  return this; }
    public Builder branch(String v)            { branch        = v;  return this; }
    public Builder changedFiles(List<String> v){ changedFiles   = v;  return this; }
    public Builder scanProfile(ScanProfile v)  { scanProfile   = v;  return this; }
    public Builder scanMode(ScanMode v)        { scanMode      = v;  return this; }
    public Builder requestedAt(Instant v)      { requestedAt   = v;  return this; }
    public Builder source(String v)            { source        = v;  return this; }
    public Builder pullRequestId(String v)     { pullRequestId = v;  return this; }

    public ScanRequest build() {
      return new ScanRequest(scanId, repositoryUrl, commitSha, branch, changedFiles,
          scanProfile, scanMode, requestedAt, source, pullRequestId);
    }
  }
}
