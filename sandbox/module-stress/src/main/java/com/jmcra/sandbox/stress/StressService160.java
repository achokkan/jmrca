package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService160 {
    public String performTask160() {
        return "Task 160 result";
    }
    
    public void crossCall(StressService161 other) {
        other.performTask161();
    }
}
