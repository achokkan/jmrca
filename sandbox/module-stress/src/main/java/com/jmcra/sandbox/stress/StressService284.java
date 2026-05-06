package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService284 {
    public String performTask284() {
        return "Task 284 result";
    }
    
    public void crossCall(StressService285 other) {
        other.performTask285();
    }
}
