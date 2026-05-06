package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService837 {
    public String performTask837() {
        return "Task 837 result";
    }
    
    public void crossCall(StressService838 other) {
        other.performTask838();
    }
}
