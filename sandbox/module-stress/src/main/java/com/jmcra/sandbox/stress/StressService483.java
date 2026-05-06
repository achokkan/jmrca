package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService483 {
    public String performTask483() {
        return "Task 483 result";
    }
    
    public void crossCall(StressService484 other) {
        other.performTask484();
    }
}
