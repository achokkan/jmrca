package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService121 {
    public String performTask121() {
        return "Task 121 result";
    }
    
    public void crossCall(StressService122 other) {
        other.performTask122();
    }
}
