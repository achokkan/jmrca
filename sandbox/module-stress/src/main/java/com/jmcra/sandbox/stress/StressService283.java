package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService283 {
    public String performTask283() {
        return "Task 283 result";
    }
    
    public void crossCall(StressService284 other) {
        other.performTask284();
    }
}
