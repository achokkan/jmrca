package com.jmcra.sandbox.res;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.kafka.annotation.KafkaListener;
import reactor.core.publisher.Mono;

/**
 * Deliberately unstable service for JMCRA testing.
 * Targets: RES-001, RES-002, RES-003.
 */
public class UnstableClient {

    // RES-001: FeignClient missing circuit breaker and fallback
    @FeignClient(name = "order-service", url = "http://order-service")
    public interface OrderFeignClient {
        @GetMapping("/orders")
        String getOrders();
    }

    private final WebClient webClient;

    public UnstableClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://payment-service").build();
    }

    /**
     * RES-002: HTTP call without explicit timeout configuration.
     */
    public Mono<String> callPayment() {
        return webClient.get()
            .uri("/pay")
            .retrieve()
            .bodyToMono(String.class); // Missing .timeout()
    }

    /**
     * RES-003: Kafka listener without retry policy or DLQ binding.
     */
    @KafkaListener(topics = "order-events", groupId = "jmcra-test")
    public void consume(String message) {
        System.out.println("Processing: " + message);
    }
}
