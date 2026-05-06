package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService285 {
    public String performTask285() {
        return "Task 285 result";
    }
    
    public void crossCall(StressService286 other) {
        other.performTask286();
    }
}
