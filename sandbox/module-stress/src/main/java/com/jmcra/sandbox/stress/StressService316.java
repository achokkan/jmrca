package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService316 {
    public String performTask316() {
        return "Task 316 result";
    }
    
    public void crossCall(StressService317 other) {
        other.performTask317();
    }
}
