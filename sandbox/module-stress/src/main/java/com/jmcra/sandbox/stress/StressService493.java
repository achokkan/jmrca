package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService493 {
    public String performTask493() {
        return "Task 493 result";
    }
    
    public void crossCall(StressService494 other) {
        other.performTask494();
    }
}
