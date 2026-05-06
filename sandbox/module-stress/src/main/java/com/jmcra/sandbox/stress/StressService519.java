package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService519 {
    public String performTask519() {
        return "Task 519 result";
    }
    
    public void crossCall(StressService520 other) {
        other.performTask520();
    }
}
