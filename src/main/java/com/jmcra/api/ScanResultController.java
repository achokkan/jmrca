package com.jmcra.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jmcra.entity.ScanEntity;
import com.jmcra.model.FindingsReport;
import com.jmcra.service.ScanHistoryService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controller for retrieving scan history and detailed reports.
 */
@RestController
@RequestMapping("/api/scans")
public class ScanResultController {

    private final ScanHistoryService scanHistoryService;

    public ScanResultController(ScanHistoryService scanHistoryService) {
        this.scanHistoryService = scanHistoryService;
    }

    @GetMapping
    public Flux<ScanEntity> listRecentScans() {
        return scanHistoryService.getRecentScans();
    }

    @GetMapping("/{scanId}/report")
    public Mono<FindingsReport> getReport(@PathVariable String scanId) {
        return scanHistoryService.getReport(scanId);
    }
}
