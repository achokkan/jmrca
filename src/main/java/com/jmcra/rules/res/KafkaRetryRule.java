package com.jmcra.rules.res;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.jmcra.model.Domain;
import com.jmcra.model.Finding;
import com.jmcra.model.Severity;
import com.jmcra.pipeline.stage4.DomainContext;
import com.jmcra.pipeline.stage4.RuleEvaluator;
import com.jmcra.rules.annotations.RuleDefinition;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * Rule Evaluator for RES-003: Missing retry pattern on KafkaListeners.
 */
@RuleDefinition(id = "RES-003", domain = Domain.RES, severity = Severity.MEDIUM)
@Component
public class KafkaRetryRule implements RuleEvaluator {

  @Override
  public String ruleId() {
    return "RES-003";
  }

  @Override
  public Severity defaultSeverity() {
    return Severity.MEDIUM;
  }

  @Override
  public Flux<Finding> evaluate(DomainContext ctx) {
    return Flux.fromStream(
        ctx.astIndex().compilationUnits().entrySet().stream()
            .flatMap(entry -> {
              String filePath = entry.getKey();
              CompilationUnit cu = entry.getValue();
              return cu.findAll(MethodDeclaration.class).stream()
                  .filter(m -> m.isAnnotationPresent("KafkaListener"))
                  .filter(m -> !m.isAnnotationPresent("Retryable"))
                  .map(stmt -> {
                      int line = stmt.getBegin().map(p -> p.line).orElse(1);
                      int col = stmt.getBegin().map(p -> p.column).orElse(1);
                      return Finding.builder(ruleId(), defaultSeverity())
                         .title("Missing retry policy on @KafkaListener")
                         .file(filePath)
                         .line(line)
                         .column(col)
                         .snippet(stmt.getDeclarationAsString(false, false, true))
                         .message("Kafka listener methods without `@Retryable` can fail silently or cause infinite offset loops (poison pill) without dead-letter strategies.")
                         .remediation("Annotate the method with `@Retryable` or configure a `DefaultErrorHandler` explicitly.")
                         .references(List.of("Spring-Kafka-Docs"))
                         .confidence(0.85)
                         .ruleVersion("1.0")
                         .build();
                  });
            })
    );
  }
}
