package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService375 {
    public String performTask375() {
        return "Task 375 result";
    }
    
    public void crossCall(StressService376 other) {
        other.performTask376();
    }
}
