package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService596 {
    public String performTask596() {
        return "Task 596 result";
    }
    
    public void crossCall(StressService597 other) {
        other.performTask597();
    }
}
