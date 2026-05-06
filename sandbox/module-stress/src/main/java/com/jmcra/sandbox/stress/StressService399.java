package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService399 {
    public String performTask399() {
        return "Task 399 result";
    }
    
    public void crossCall(StressService400 other) {
        other.performTask400();
    }
}
