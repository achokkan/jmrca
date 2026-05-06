package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService15 {
    public String performTask15() {
        return "Task 15 result";
    }
    
    public void crossCall(StressService16 other) {
        other.performTask16();
    }
}
