package com.jmcra.rules.des;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
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
 * Rule Evaluator for DES-001: God Class (Public Method Bounds)
 */
@RuleDefinition(id = "DES-001", domain = Domain.DES, severity = Severity.MEDIUM)
@Component
public class GodClassRule implements RuleEvaluator {

  @Override
  public String ruleId() {
    return "DES-001";
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
              return cu.findAll(ClassOrInterfaceDeclaration.class).stream()
                  .filter(c -> !c.isInterface())
                  .filter(c -> {
                      long publicMethods = c.getMethods().stream().filter(MethodDeclaration::isPublic).count();
                      return publicMethods > 10;
                  })
                  .map(stmt -> {
                      int line = stmt.getBegin().map(p -> p.line).orElse(1);
                      int col = stmt.getBegin().map(p -> p.column).orElse(1);
                      return Finding.builder(ruleId(), defaultSeverity())
                         .title("Class with >10 public methods (SRP violation)")
                         .file(filePath)
                         .line(line)
                         .column(col)
                         .snippet(stmt.getNameAsString())
                         .message("This class exposes more than 10 public methods (" + stmt.getMethods().stream().filter(MethodDeclaration::isPublic).count() + "), exhibiting strong \"God Class\" / Single Responsibility Principle (SRP) violations resulting in difficult testing and coupling.")
                         .remediation("Refactor domain responsibilities into smaller, distinct classes injecting dependencies securely.")
                         .references(List.of("SOLID-SRP", "Martin-Clean-Code"))
                         .confidence(0.95)
                         .ruleVersion("1.0")
                         .build();
                  });
            })
    );
  }
}
