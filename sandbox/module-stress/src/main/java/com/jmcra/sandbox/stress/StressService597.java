package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService597 {
    public String performTask597() {
        return "Task 597 result";
    }
    
    public void crossCall(StressService598 other) {
        other.performTask598();
    }
}
