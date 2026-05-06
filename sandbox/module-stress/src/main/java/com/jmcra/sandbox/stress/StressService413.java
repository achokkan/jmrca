package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService413 {
    public String performTask413() {
        return "Task 413 result";
    }
    
    public void crossCall(StressService414 other) {
        other.performTask414();
    }
}
