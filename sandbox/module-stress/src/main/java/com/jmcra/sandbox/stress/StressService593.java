package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService593 {
    public String performTask593() {
        return "Task 593 result";
    }
    
    public void crossCall(StressService594 other) {
        other.performTask594();
    }
}
