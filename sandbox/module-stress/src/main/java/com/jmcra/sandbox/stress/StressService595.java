package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService595 {
    public String performTask595() {
        return "Task 595 result";
    }
    
    public void crossCall(StressService596 other) {
        other.performTask596();
    }
}
