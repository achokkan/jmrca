package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService109 {
    public String performTask109() {
        return "Task 109 result";
    }
    
    public void crossCall(StressService110 other) {
        other.performTask110();
    }
}
