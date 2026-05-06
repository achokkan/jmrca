package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService143 {
    public String performTask143() {
        return "Task 143 result";
    }
    
    public void crossCall(StressService144 other) {
        other.performTask144();
    }
}
