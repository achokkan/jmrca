package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService14 {
    public String performTask14() {
        return "Task 14 result";
    }
    
    public void crossCall(StressService15 other) {
        other.performTask15();
    }
}
