package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService450 {
    public String performTask450() {
        return "Task 450 result";
    }
    
    public void crossCall(StressService451 other) {
        other.performTask451();
    }
}
