package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService34 {
    public String performTask34() {
        return "Task 34 result";
    }
    
    public void crossCall(StressService35 other) {
        other.performTask35();
    }
}
