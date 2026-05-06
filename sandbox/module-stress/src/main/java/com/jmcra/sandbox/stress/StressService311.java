package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService311 {
    public String performTask311() {
        return "Task 311 result";
    }
    
    public void crossCall(StressService312 other) {
        other.performTask312();
    }
}
