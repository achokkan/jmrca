package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService233 {
    public String performTask233() {
        return "Task 233 result";
    }
    
    public void crossCall(StressService234 other) {
        other.performTask234();
    }
}
