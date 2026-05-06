package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService314 {
    public String performTask314() {
        return "Task 314 result";
    }
    
    public void crossCall(StressService315 other) {
        other.performTask315();
    }
}
