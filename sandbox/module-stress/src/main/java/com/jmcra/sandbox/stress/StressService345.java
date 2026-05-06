package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService345 {
    public String performTask345() {
        return "Task 345 result";
    }
    
    public void crossCall(StressService346 other) {
        other.performTask346();
    }
}
