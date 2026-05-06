package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService279 {
    public String performTask279() {
        return "Task 279 result";
    }
    
    public void crossCall(StressService280 other) {
        other.performTask280();
    }
}
