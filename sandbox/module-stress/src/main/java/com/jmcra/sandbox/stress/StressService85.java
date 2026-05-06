package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService85 {
    public String performTask85() {
        return "Task 85 result";
    }
    
    public void crossCall(StressService86 other) {
        other.performTask86();
    }
}
