package com.jmcra.rules.msg;

import com.jmcra.annotations.RuleContractTest;
import com.jmcra.annotations.ShouldFind;
import com.jmcra.annotations.ShouldNotFind;
import com.jmcra.annotations.SpecEvidence;
import com.jmcra.model.Severity;

/**
 * Tests for MSG-008: KRaft ZooKeeper configuration detection (Kafka 4).
 * SPC-111: MSG-008 KRaft ZooKeeper check
 */
@RuleContractTest(specClause = "SPC-111", ruleId = "MSG-008")
class KRaftZooKeeperContractTest {

  @ShouldFind(severity = Severity.HIGH, minConfidence = 0.90)
  @SpecEvidence("Starting in Kafka 4, ZooKeeper is completely removed in favor of KRaft.")
  static final String MIGRATION_ZOOKEEPER = """
      package com.acme;
      import java.util.Map;
      import java.util.HashMap;
      
      public class KafkaConfig {
          public Map<String, Object> clusterConfigs() {
              Map<String, Object> props = new HashMap<>();
              props.put("zookeeper.connect", "localhost:2181");
              return props;
          }
      }
      """;

  @ShouldNotFind
  @SpecEvidence("bootstrap.servers is the correct KRaft connection pattern.")
  static final String KRAFT_BOOTSTRAP = """
      package com.acme;
      import org.apache.kafka.clients.producer.ProducerConfig;
      import java.util.Map;
      import java.util.HashMap;
      
      public class KafkaConfig {
          public Map<String, Object> clusterConfigs() {
              Map<String, Object> props = new HashMap<>();
              props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
              return props;
          }
      }
      """;
}
