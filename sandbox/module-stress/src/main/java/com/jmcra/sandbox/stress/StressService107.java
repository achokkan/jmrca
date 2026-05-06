package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService107 {
    public String performTask107() {
        return "Task 107 result";
    }
    
    public void crossCall(StressService108 other) {
        other.performTask108();
    }
}
