package com.jmcra.rules.msg;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.jmcra.model.Domain;
import com.jmcra.model.Finding;
import com.jmcra.model.Severity;
import com.jmcra.pipeline.stage4.DomainContext;
import com.jmcra.pipeline.stage4.RuleEvaluator;
import com.jmcra.rules.annotations.Framework;
import com.jmcra.rules.annotations.RuleDefinition;
import com.jmcra.rules.annotations.VersionGate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * Rule Evaluator for MSG-008: ZooKeeper configuration references in Kafka 4+.
 */
@RuleDefinition(id = "MSG-008", domain = Domain.MSG, severity = Severity.HIGH, sinceVersion = @VersionGate(framework = Framework.SPRING_BOOT, minVersion = "4.0.0"))
@Component
public class KRaftZooKeeperRule implements RuleEvaluator {

  @Override
  public String ruleId() {
    return "MSG-008";
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
              return cu.findAll(StringLiteralExpr.class).stream()
                  .filter(s -> s.getValue().contains("zookeeper.connect") ||
                               s.getValue().contains("zookeeper.sasl"))
                  .map(stmt -> {
                      int line = stmt.getBegin().map(p -> p.line).orElse(1);
                      int col = stmt.getBegin().map(p -> p.column).orElse(1);
                      return Finding.builder(ruleId(), defaultSeverity())
                         .title("ZooKeeper configuration detected in Kafka 4 KRaft environment")
                         .file(filePath)
                         .line(line)
                         .column(col)
                         .snippet(stmt.toString())
                         .message("Kafka 4 completely removes ZooKeeper in favor of KRaft. The `zookeeper.connect` property is invalid and indicates a migration gap.")
                         .remediation("Use `bootstrap.servers` to connect to KRaft controller/broker endpoints.")
                         .references(List.of("KIP-833"))
                         .confidence(0.99)
                         .ruleVersion("1.0")
                         .build();
                  });
            })
    );
  }
}
