package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService565 {
    public String performTask565() {
        return "Task 565 result";
    }
    
    public void crossCall(StressService566 other) {
        other.performTask566();
    }
}
