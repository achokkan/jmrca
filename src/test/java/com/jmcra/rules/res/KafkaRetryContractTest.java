package com.jmcra.rules.res;

import com.jmcra.annotations.RuleContractTest;
import com.jmcra.annotations.ShouldFind;
import com.jmcra.annotations.ShouldNotFind;
import com.jmcra.annotations.SpecEvidence;
import com.jmcra.model.Severity;

/**
 * Tests for RES-003: Missing retry policy on @KafkaListener
 * SPC-102: RES-003 Kafka listener resilience
 */
@RuleContractTest(specClause = "SPC-102", ruleId = "RES-003")
class KafkaRetryContractTest {

  @ShouldFind(severity = Severity.MEDIUM, minConfidence = 0.85)
  @SpecEvidence("A default KafkaListener can infinitely block partitions on deserialization/database errors.")
  static final String MISSING_RETRY = """
      package com.acme;
      import org.springframework.kafka.annotation.KafkaListener;
      import org.springframework.stereotype.Service;
      
      @Service
      public class EventConsumer {
          @KafkaListener(topics = "orders")
          public void handle(String order) {
              process(order);
          }
      }
      """;

  @ShouldNotFind
  @SpecEvidence("Adding @Retryable ensures the message processing is retried before failing out.")
  static final String WITH_RETRY = """
      package com.acme;
      import org.springframework.kafka.annotation.KafkaListener;
      import org.springframework.retry.annotation.Retryable;
      import org.springframework.stereotype.Service;
      
      @Service
      public class EventConsumer {
          @Retryable(maxAttempts = 3)
          @KafkaListener(topics = "orders")
          public void handle(String order) {
              process(order);
          }
      }
      """;
}
