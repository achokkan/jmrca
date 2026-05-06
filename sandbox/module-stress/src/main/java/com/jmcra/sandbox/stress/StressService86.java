package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService86 {
    public String performTask86() {
        return "Task 86 result";
    }
    
    public void crossCall(StressService87 other) {
        other.performTask87();
    }
}
