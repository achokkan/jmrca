package com.jmcra.sandbox.res;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import reactor.core.publisher.Mono;
import java.time.Duration;

/**
 * Compliant resilient service for JMCRA testing.
 */
public class ResilientConsumer {

    // RES-001 Compliant: Circuit breaker and fallback defined
    @FeignClient(name = "user-service", url = "http://user-service", fallback = UserFallback.class)
    public interface UserFeignClient {
        @GetMapping("/users")
        @CircuitBreaker(name = "userService")
        String getUsers();
    }

    public static class UserFallback implements UserFeignClient {
        @Override public String getUsers() { return "fallback-users"; }
    }

    private final WebClient webClient;

    public ResilientConsumer(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://inventory-service").build();
    }

    /**
     * RES-002 Compliant: Explicit timeout set.
     */
    public Mono<String> checkInventory() {
        return webClient.get()
            .uri("/stock")
            .retrieve()
            .bodyToMono(String.class)
            .timeout(Duration.ofSeconds(3)); // Explicit timeout
    }

    /**
     * RES-003 Compliant: Kafka listener with retry and DLQ.
     */
    @RetryableTopic(
        attempts = "3",
        backoff = @Backoff(delay = 1000, multiplier = 2.0),
        dltStrategy = org.springframework.kafka.retrytopic.DltStrategy.FAIL_ON_ERROR
    )
    @KafkaListener(topics = "payment-events", groupId = "jmcra-resilient")
    public void handlePayment(String message) {
        System.out.println("Processing payment: " + message);
    }
}
