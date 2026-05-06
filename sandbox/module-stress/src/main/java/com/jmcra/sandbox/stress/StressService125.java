package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService125 {
    public String performTask125() {
        return "Task 125 result";
    }
    
    public void crossCall(StressService126 other) {
        other.performTask126();
    }
}
