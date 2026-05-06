package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService988 {
    public String performTask988() {
        return "Task 988 result";
    }
    
    public void crossCall(StressService989 other) {
        other.performTask989();
    }
}
