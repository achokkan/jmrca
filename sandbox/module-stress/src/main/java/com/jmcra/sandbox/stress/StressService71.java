package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService71 {
    public String performTask71() {
        return "Task 71 result";
    }
    
    public void crossCall(StressService72 other) {
        other.performTask72();
    }
}
