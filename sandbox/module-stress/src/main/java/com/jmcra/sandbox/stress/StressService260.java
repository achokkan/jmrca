package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService260 {
    public String performTask260() {
        return "Task 260 result";
    }
    
    public void crossCall(StressService261 other) {
        other.performTask261();
    }
}
