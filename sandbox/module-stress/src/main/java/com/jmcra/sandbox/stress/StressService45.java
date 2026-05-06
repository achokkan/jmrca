package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService45 {
    public String performTask45() {
        return "Task 45 result";
    }
    
    public void crossCall(StressService46 other) {
        other.performTask46();
    }
}
