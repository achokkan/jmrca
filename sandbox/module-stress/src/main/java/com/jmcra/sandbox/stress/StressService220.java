package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService220 {
    public String performTask220() {
        return "Task 220 result";
    }
    
    public void crossCall(StressService221 other) {
        other.performTask221();
    }
}
