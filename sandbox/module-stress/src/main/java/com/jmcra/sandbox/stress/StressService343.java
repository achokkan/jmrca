package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService343 {
    public String performTask343() {
        return "Task 343 result";
    }
    
    public void crossCall(StressService344 other) {
        other.performTask344();
    }
}
