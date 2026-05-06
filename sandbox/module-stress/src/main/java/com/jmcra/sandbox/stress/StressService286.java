package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService286 {
    public String performTask286() {
        return "Task 286 result";
    }
    
    public void crossCall(StressService287 other) {
        other.performTask287();
    }
}
