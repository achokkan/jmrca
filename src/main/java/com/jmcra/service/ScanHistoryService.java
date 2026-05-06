package com.jmcra.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmcra.entity.ScanEntity;
import com.jmcra.model.FindingsReport;
import com.jmcra.model.ScanRequest;
import com.jmcra.repository.ScanRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service to manage scan history and results in the database.
 */
@Service
public class ScanHistoryService {

    private static final Logger log = LoggerFactory.getLogger(ScanHistoryService.class);

    private final ScanRepository scanRepository;
    private final ObjectMapper objectMapper;

    public ScanHistoryService(ScanRepository scanRepository, ObjectMapper objectMapper) {
        this.scanRepository = scanRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Records the start of a scan.
     */
    public Mono<ScanEntity> recordScanStart(ScanRequest request) {
        ScanEntity entity = new ScanEntity();
        entity.setId(UUID.fromString(request.scanId()));
        entity.setRepositoryUrl(request.repositoryUrl());
        entity.setCommitSha(request.commitSha());
        entity.setBranch(request.branch());
        entity.setStatus("STARTED");
        entity.setStartedAt(request.requestedAt());
        entity.setAsNew(true);

        return scanRepository.save(entity)
            .doOnSuccess(saved -> {
                if (saved != null) {
                    log.debug("[{}] Scan recorded as STARTED", saved.getId());
                }
            });
    }

    /**
     * Records the completion of a scan with the final report.
     */
    public Mono<ScanEntity> recordScanCompletion(FindingsReport report) {
        return scanRepository.findById(UUID.fromString(report.scanId()))
            .flatMap(entity -> {
                entity.setStatus("COMPLETED");
                entity.setCompletedAt(report.scanCompletedAt());
                entity.setHealthScore(BigDecimal.valueOf(report.healthScore().score()));
                entity.setGateViolated(report.gateViolated());
                try {
                    String json = objectMapper.writeValueAsString(report);
                    log.debug("[{}] Serialized report size: {} chars", report.scanId(), json.length());
                    entity.setReport(json);
                } catch (JsonProcessingException e) {
                    log.error("[{}] Mapping error during recordScanCompletion: {}", report.scanId(), e.getMessage(), e);
                    // Proactively fail the chain so we see the error
                    return Mono.error(new RuntimeException("Report serialization failed", e));
                }
                return scanRepository.save(entity);
            })
            .doOnSuccess(saved -> {
                if (saved != null) {
                    log.info("[{}] Scan recorded as COMPLETED — score={}", 
                        saved.getId(), saved.getHealthScore());
                } else {
                    log.warn("[{}] recordScanCompletion called but scan not found in DB", report.scanId());
                }
            });
    }

    /**
     * Records a scan failure.
     */
    public Mono<ScanEntity> recordScanFailure(String scanId, String reason) {
        return scanRepository.findById(UUID.fromString(scanId))
            .flatMap(entity -> {
                entity.setStatus("FAILED");
                entity.setCompletedAt(java.time.Instant.now());
                // Optionally store the reason in a separate column or in reportJson
                return scanRepository.save(entity);
            })
            .doOnError(e -> log.error("[{}] Failed to record scan failure: {}", scanId, e.getMessage()));
    }

    /**
     * Retrieves all recent scans.
     */
    public Flux<ScanEntity> getRecentScans() {
        return scanRepository.findAllByOrderByStartedAtDesc();
    }

    /**
     * Retrieves a full report by scanId.
     */
    public Mono<FindingsReport> getReport(String scanId) {
        return scanRepository.findById(UUID.fromString(scanId))
            .flatMap(entity -> {
                if (entity.getReport() == null) return Mono.empty();
                try {
                    return Mono.just(objectMapper.readValue(entity.getReport(), FindingsReport.class));
                } catch (JsonProcessingException e) {
                    return Mono.error(e);
                }
            });
    }
}
