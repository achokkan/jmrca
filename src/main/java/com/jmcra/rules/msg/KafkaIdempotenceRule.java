package com.jmcra.rules.msg;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
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
 * Rule Evaluator for MSG-001: Kafka Producer Idempotence disabled.
 */
@RuleDefinition(id = "MSG-001", domain = Domain.MSG, severity = Severity.HIGH)
@Component
public class KafkaIdempotenceRule implements RuleEvaluator {

  @Override
  public String ruleId() {
    return "MSG-001";
  }

  @Override
  public Severity defaultSeverity() {
    return Severity.HIGH;
  }

  @Override
  public Flux<Finding> evaluate(DomainContext ctx) {
    return Flux.fromStream(
        ctx.astIndex().compilationUnits().entrySet().stream()
            .flatMap(entry -> {
              String filePath = entry.getKey();
              CompilationUnit cu = entry.getValue();
              return cu.findAll(MethodCallExpr.class).stream()
                  .filter(m -> m.getNameAsString().equals("put"))
                  .filter(m -> m.getArguments().size() == 2)
                  .filter(m -> {
                      String arg0 = m.getArgument(0).toString();
                      return arg0.equals("ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG") || arg0.equals("\"enable.idempotence\"");
                  })
                  .filter(m -> m.getArgument(1).toString().equals("false"))
                  .map(stmt -> {
                      int line = stmt.getBegin().map(p -> p.line).orElse(1);
                      int col = stmt.getBegin().map(p -> p.column).orElse(1);
                      return Finding.builder(ruleId(), defaultSeverity())
                         .title("Kafka producer idempotence explicitly disabled")
                         .file(filePath)
                         .line(line)
                         .column(col)
                         .snippet(stmt.toString())
                         .message("Disabling `enable.idempotence` (which defaults to true in Kafka >= 3.0) forces the producer into strict at-least-once delivery, risking message duplication upon network retries.")
                         .remediation("Remove the configuration to inherit the safe default, or explicitly set it to `true`.")
                         .references(List.of("Exactly-Once-Semantics"))
                         .confidence(0.95)
                         .ruleVersion("1.0")
                         .build();
                  });
            })
    );
  }
}
