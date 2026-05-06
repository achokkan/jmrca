package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService243 {
    public String performTask243() {
        return "Task 243 result";
    }
    
    public void crossCall(StressService244 other) {
        other.performTask244();
    }
}
