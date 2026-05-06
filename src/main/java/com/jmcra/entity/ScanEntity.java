package com.jmcra.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Persistent entity for a JMCRA scan.
 * Maps to the 'scans' table in PostgreSQL.
 */
@Table("scans")
public class ScanEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    private String repositoryUrl;
    private String commitSha;
    private String branch;
    private String status;
    private BigDecimal healthScore;
    private boolean gateViolated;
    private String report; // Full FindingsReport serialized as JSON String
    private Instant startedAt;
    private Instant completedAt;

    @Transient
    private boolean isNew = false;

    // Getters and Setters

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getRepositoryUrl() { return repositoryUrl; }
    public void setRepositoryUrl(String repositoryUrl) { this.repositoryUrl = repositoryUrl; }

    public String getCommitSha() { return commitSha; }
    public void setCommitSha(String commitSha) { this.commitSha = commitSha; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getHealthScore() { return healthScore; }
    public void setHealthScore(BigDecimal healthScore) { this.healthScore = healthScore; }

    public boolean isGateViolated() { return gateViolated; }
    public void setGateViolated(boolean gateViolated) { this.gateViolated = gateViolated; }
    
    public String getReport() { return report; }
	public void setReport(String report) { this.report = report; }

	public Instant getStartedAt() { return startedAt; }
    public void setStartedAt(Instant startedAt) { this.startedAt = startedAt; }

    public Instant getCompletedAt() { return completedAt; }
    public void setCompletedAt(Instant completedAt) { this.completedAt = completedAt; }

    @Override
    public boolean isNew() {
        return isNew;
    }

    public void setAsNew(boolean isNew) {
        this.isNew = isNew;
    }
}
