package com.jmcra.rules.msg;

import com.jmcra.annotations.RuleContractTest;
import com.jmcra.annotations.ShouldFind;
import com.jmcra.annotations.ShouldNotFind;
import com.jmcra.annotations.SpecEvidence;
import com.jmcra.model.Severity;

/**
 * Tests for MSG-001: Kafka producer missing idempotent=true configuration
 * SPC-110: MSG-001 Idempotent Producer
 */
@RuleContractTest(specClause = "SPC-110", ruleId = "MSG-001")
class KafkaIdempotenceContractTest {

  @ShouldFind(severity = Severity.HIGH, minConfidence = 0.90)
  @SpecEvidence("Disabling idempotence forces strict at-least-once with duplications on retries.")
  static final String DISABLED_IDEMPOTENCE = """
      package com.acme;
      import org.apache.kafka.clients.producer.ProducerConfig;
      import java.util.Map;
      import java.util.HashMap;
      
      public class KafkaConfig {
          public Map<String, Object> producerConfigs() {
              Map<String, Object> props = new HashMap<>();
              props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, false);
              return props;
          }
      }
      """;

  @ShouldNotFind
  @SpecEvidence("Default or explicit true is safe.")
  static final String ENABLED_IDEMPOTENCE = """
      package com.acme;
      import org.apache.kafka.clients.producer.ProducerConfig;
      import java.util.Map;
      import java.util.HashMap;
      
      public class KafkaConfig {
          public Map<String, Object> producerConfigs() {
              Map<String, Object> props = new HashMap<>();
              props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
              return props;
          }
      }
      """;
}
