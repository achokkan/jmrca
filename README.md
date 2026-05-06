# JMCRA — Java Microservices Code Review Agent

JMCRA is a spec-driven, highly concurrent, reactive static analysis and automated code review agent tailored for large-scale enterprise Java microservices ecosystems.

It blends strict Abstract Syntax Tree (AST) analysis via JavaParser with strategic LLM orchestration via Project Reactor to provide fast, reliable, and deeply context-aware security, resiliency, data, and concurrency code reviews.

---

## 🚀 Key Features

- **Reactive Pipeline**: Built using Spring Boot 4.0 and Project Reactor for non-blocking stream execution spanning 6 decoupled stages from ingestion to delivery.
- **Deep AST Engine**: Uncovers complex flaws (e.g. Structured Concurrency leaks, N+1 Selects, Kafka Misconfigurations) silently missed by traditional regex-driven linters.
- **Spec-Driven Rule Engine**: Enforces rigid contract boundaries. Integrates strict `@RuleDefinition` lifecycle and framework version gating (e.g., skips Java 25 scoped-variable checks on Java 17 codebases). 
- **LLM Reasoning**: Pluggable LLM fallback (Claude, OpenAI, Gemini) to resolve complex semantic ambiguities that pure AST rules cannot confidently classify.
- **Security & Ecosystem Gatekeeper**: Catches highly specific Spring Cloud, Project Reactor, and Java 25 edge cases alongside generalized security analysis (OWASP).

---

## 🏗️ Technical Stack

- **Platform**: Java 25 LTS (Features: JEP 505 / Structured Concurrency)
- **Framework**: Spring Boot 4.0.0, Project Reactor (Netty)
- **AST Parser**: JavaParser 3.28; JGraphT for Call-Graph traversal
- **Build**: Maven 3.14+
- **Test**: JUnit 5, Reactor Test, JaCoCo, AssertJ, Golden Testing.

---

## ⚙️ Quick Start

### 1. Prerequisites
- **Java**: JDK 25 installed and configured on your `PATH`.
- **Maven**: Latest Maven version.
- **API Keys**: Depending on your integrations, ensure you have API keys exported for Slack, GitHub, or your chosen LLM provider.

### 2. Build the Project
```bash
mvn clean install
```
*(The build includes comprehensive Pipeline Contract Tests (PCTs) and JaCoCo coverage boundaries).*

### 3. Run the Agent
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

To run in Docker via Docker Compose (assuming images are pre-built):
```bash
docker-compose up -d
```

---

## 🔌 Environment Variables & Configuration

Configuration is managed via `application.yml` nested under the `jmcra.*` prefix. Critical secrets should be exported locally:

| Variable | Description |
|---|---|
| `X_HUB_SIGNATURE_SECRET` | Secret injected to validate GitHub Webhook payloads (Stage 1). |
| `ANTHROPIC_API_KEY` | (Optional) Token for Claude interactions to resolve ambiguity (Stage 3). |
| `OPENAI_API_KEY` | (Optional) Token for OpenAI mapping (Stage 3). |
| `JMCRA_GITHUB_TOKEN` | Token for delivering inline review comments back to a GitHub PR (Stage 6). |
| `JMCRA_SLACK_WEBHOOK` | Webhook URI for high-level aggregated delivery cards (Stage 6). |

---

## 📖 Deeper Architecture & Wiki
For a comprehensive dive into how the 6-stage reactive pipeline operates, how rules use JavaParser internally, and how Contract testing locks behavior guarantees across versions, please consult the **[Detailed Engineering Wiki](docs/WIKI.md)**.
