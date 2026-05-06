package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService198 {
    public String performTask198() {
        return "Task 198 result";
    }
    
    public void crossCall(StressService199 other) {
        other.performTask199();
    }
}
