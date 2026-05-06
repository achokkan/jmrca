package com.jmcra.pipeline;

import com.jmcra.model.FindingsReport;
import com.jmcra.model.WebhookPayload;
import com.jmcra.pipeline.stage1.IngestService;
import com.jmcra.pipeline.stage2.ParseIndexService;
import com.jmcra.pipeline.stage3.AnalysisDispatchService;
import com.jmcra.pipeline.stage5.RankDedupeService;
import com.jmcra.pipeline.stage6.DeliveryService;
import com.jmcra.service.ScanHistoryService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Orchestrates the complete 6-stage JMCRA pipeline.
 *
 * <pre>
 * Stage 1 — Ingest       : WebhookPayload → ScanRequest
 * Stage 2 — Parse/Index  : ScanRequest → DomainContext
 * Stage 3 — Dispatch     : DomainContext → Flux&lt;Finding&gt; (parallel domain workers)
 * Stage 4 — (inline)     : RuleEvaluator.evaluate(ctx) called within Stage 3
 * Stage 5 — Rank/Dedupe  : List&lt;Finding&gt; → FindingsReport
 * Stage 6 — Deliver      : FindingsReport → targets (GitHub, JIRA, Slack, CI gate)
 * </pre>
 *
 * The entire chain is non-blocking and reactive (Project Reactor 3.8, ADR-002).
 * <p>
 * Spec: Section 4 (Agent Architecture — High-Level Pipeline).
 */
@Service
public class PipelineOrchestrator {

  private static final Logger log = LoggerFactory.getLogger(PipelineOrchestrator.class);

  private final IngestService           ingestService;
  private final ParseIndexService       parseIndexService;
  private final AnalysisDispatchService analysisDispatchService;
  private final RankDedupeService       rankDedupeService;
  private final DeliveryService         deliveryService;
  private final ScanHistoryService      scanHistoryService;
  private final MeterRegistry           meterRegistry;

  public PipelineOrchestrator(
      IngestService ingestService,
      ParseIndexService parseIndexService,
      AnalysisDispatchService analysisDispatchService,
      RankDedupeService rankDedupeService,
      DeliveryService deliveryService,
      ScanHistoryService scanHistoryService,
      MeterRegistry meterRegistry) {
    this.ingestService           = ingestService;
    this.parseIndexService       = parseIndexService;
    this.analysisDispatchService = analysisDispatchService;
    this.rankDedupeService       = rankDedupeService;
    this.deliveryService         = deliveryService;
    this.scanHistoryService      = scanHistoryService;
    this.meterRegistry           = meterRegistry;
  }

  /**
   * Runs the full 6-stage pipeline against the given webhook payload.
   *
   * @param payload Raw webhook envelope received by {@code WebhookController}.
   * @return A {@code Mono} emitting the final {@link FindingsReport} after all delivery completes.
   */
  public Mono<FindingsReport> run(WebhookPayload payload) {
    return ingestService.ingest(payload)
        .flatMap(this::runFromRequest);
  }

  /**
   * Runs the pipeline starting from Stage 2 using a pre-ingested request.
   */
  public Mono<FindingsReport> runFromRequest(com.jmcra.model.ScanRequest req) {
    Timer.Sample timerSample = Timer.start(meterRegistry);

    return scanHistoryService.recordScanStart(req)
        .switchIfEmpty(Mono.defer(() -> {
          log.warn("[{}] recordScanStart returned empty Mono — continuing analysis regardless", req.scanId());
          return Mono.empty();
        }))
        .then(parseIndexService.parseAndIndex(req))             // Stage 2 → DomainContext
        .flatMapMany(ctx ->
            analysisDispatchService.dispatch(ctx)            // Stage 3+4 → Flux<Finding>
                .collectList()
                .flatMap(findings -> rankDedupeService
                    .rankAndDedupe(findings, ctx))           // Stage 5 → FindingsReport
        )
        .next()
        .flatMap(deliveryService::deliver)                   // Stage 6 → deliver
        .flatMap(report -> scanHistoryService.recordScanCompletion(report).thenReturn(report))
        .doOnSuccess(report -> {
          timerSample.stop(meterRegistry.timer("jmcra.pipeline.duration",
              "mode", report.scanMode().name()));
          log.info("[{}] Pipeline complete — score={}, findings={}, gate={}",
              report.scanId(),
              String.format("%.1f", report.healthScore().score()),
              report.findings().size(),
              report.gateViolated() ? "VIOLATED" : "OK");
          meterRegistry.counter("jmcra.scans.completed").increment();
        })
        .doOnError(e -> {
          meterRegistry.counter("jmcra.scans.failed").increment();
          log.error("Pipeline failed: {}", e.getMessage(), e);
          scanHistoryService.recordScanFailure(req.scanId(), e.getMessage()).subscribe();
        });
  }
}
