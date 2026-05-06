package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService72 {
    public String performTask72() {
        return "Task 72 result";
    }
    
    public void crossCall(StressService73 other) {
        other.performTask73();
    }
}
