package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService183 {
    public String performTask183() {
        return "Task 183 result";
    }
    
    public void crossCall(StressService184 other) {
        other.performTask184();
    }
}
