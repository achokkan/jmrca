package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService987 {
    public String performTask987() {
        return "Task 987 result";
    }
    
    public void crossCall(StressService988 other) {
        other.performTask988();
    }
}
