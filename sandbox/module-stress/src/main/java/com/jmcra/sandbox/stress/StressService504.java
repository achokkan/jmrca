package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService504 {
    public String performTask504() {
        return "Task 504 result";
    }
    
    public void crossCall(StressService505 other) {
        other.performTask505();
    }
}
