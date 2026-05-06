package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService76 {
    public String performTask76() {
        return "Task 76 result";
    }
    
    public void crossCall(StressService77 other) {
        other.performTask77();
    }
}
