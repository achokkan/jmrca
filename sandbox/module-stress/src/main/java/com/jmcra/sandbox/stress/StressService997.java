package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService997 {
    public String performTask997() {
        return "Task 997 result";
    }
    
    public void crossCall(StressService998 other) {
        other.performTask998();
    }
}
