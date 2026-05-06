package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService53 {
    public String performTask53() {
        return "Task 53 result";
    }
    
    public void crossCall(StressService54 other) {
        other.performTask54();
    }
}
