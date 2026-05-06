package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService503 {
    public String performTask503() {
        return "Task 503 result";
    }
    
    public void crossCall(StressService504 other) {
        other.performTask504();
    }
}
