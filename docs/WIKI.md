# JMCRA Detailed Architecture Wiki

Welcome to the internal engineering documentation for the Java Microservices Code Review Agent (JMCRA).

---

## 1. Core Architecture: The 6-Stage Reactive Pipeline

JMCRA is architected as a non-blocking stream processor powered by **Project Reactor** (`Flux/Mono`). Every code push or Pull Request seamlessly flows through exactly 6 discrete states.

### Stage 1: Ingest (`WebhookController`)
*   **Role:** Exposes REST endpoints to consume Webhook events (GitHub, GitLab, Scheduled Triggers).
*   **Security:** Enforces strict HMAC `SHA-256` signature verification to immediately dump unauthenticated network traffic.
*   **Output:** Constructs a strongly-typed `ScanRequest` context encapsulating branch, SHA, and scan mode logic, then broadcasts an internal domain `ScanStarted` event.

### Stage 2: Parse (`AstIndexer` & `JGraphT`)
*   **Role:** Clones the target source-code branch locally and aggressively parses the Java files using **JavaParser**.
*   **Output:** Assembles a robust, thread-safe `AstIndex` containing `CompilationUnit` objects for all `.java` files along with a mapped Class and Method layout to trace deep semantic behavior.

### Stage 3: LLM Context Enrichment (`ContextOrchestrator`)
*   **Role:** Analyzes the target directory structure and gathers the specific "golden specification" docs (e.g., `sdd-security.md`).
*   **Output:** Bundles these constraints directly alongside the source code snippets. If ambiguity exists (for example, whether an object handles "PII"), the internal rules engine can dispatch synchronous/asynchronous prompts via `LlmClientFactory` (Claude, OpenAI) to gain high-confidence classification scoring.

### Stage 4: Analysis Dispatch (`DomainContext` & `RuleCatalogLoader`)
*   **Role:** Bootstraps rules across 9 logical domains (SEC, RES, OBS, DAT, API, MSG, CON, DES, DEP) based on the immutable definition mappings contained inside `catalog.json`.
*   **Engine mechanics:** Uses structural reflections and recursive descent AST matching mechanisms. 
*   **Version Limiter:** If a repository is running Spring Boot 2.0, rules wrapped in `@VersionGate(minVersion = "4.0.0")` gracefully skip evaluation. 

### Stage 5: Rank & Dedupe (`HealthScore` & `Deduplicator`)
*   **Role:** Takes the `Flux<Finding>` outputs from Stage 4 and drops redundant false-positives.
*   **Interop:** Evaluates rules against potential SonarQube baseline thresholds.
*   **Output:** Compiles an ultimate `FindingsReport` mapping a weighted **Health Score**. A `perfect()` score implies complete domain compliance, while CRITICAL errors drop scores massively resulting in CI gates breaking. 

### Stage 6: Delivery (`DeliveryTarget` Adapters)
*   **Role:** Fans out the finalised `FindingsReport` across downstream protocols.
*   **Targets:** 
    *   **GitHub**: Uses GitHub Checks API (`/repos/{owner}/{repo}/check-runs`) to output rich Markdown annotations directly onto PR views.
    *   **Slack**: Emits JSON Block Kit payloads delivering a quick `top-5` severity card breakdown.
    *   **Jira**: Emits sub-tasks based directly off CRITICAL tracking.

---

## 2. Rule Authoring Configuration

Creating rules in JMCRA is designed to be frictionless while enforcing static integrity via Java annotations mappings that couple exactly to `rules/catalog.json`.

### `@RuleDefinition`
Every static analyzer maps to an implementation of `RuleEvaluator`. It must be annotated with `@RuleDefinition`:

```java
@RuleDefinition(
    id = "SEC-012", 
    domain = Domain.SEC, 
    severity = Severity.HIGH, 
    version = "1.0",
    sinceVersion = @VersionGate(framework = Framework.JAVA, minVersion = "25")
)
```

The attributes map exactly to `catalog.json` boundaries:
*   `id`: `DOM-NNN` Stable format rules. Modifying this breaches contract rules.
*   `version`: Exists to track iteration on the rule AST parsing logic over time.
*   `sinceVersion`: Integrates `@VersionGate`. Prevents rules attempting to parse Java 25 `StructuedTaskScope` syntax on Java 17 runtimes.

---

## 3. Strict Traceability (Contract Delivery)

JMCRA commits strictly to a unified methodology. *No rule may exist without a traceability mapping tracing back to the overarching Systems Design Document (SDD).*

### Traceability Matrix
Located in `docs/traceability-matrix.csv`, every rule definition lists precisely its origin specification reference (e.g. `SPC-035` mapping to `OBS-001 (TracePropagationRule)`).

### `@RuleContractTest` Protocol
Every rule must pass its localized Oracle testing enforcing "RED-first tests". This asserts exactly what AST logic should trigger positive metrics, and structurally what code shapes should be gracefully bypassed. 
