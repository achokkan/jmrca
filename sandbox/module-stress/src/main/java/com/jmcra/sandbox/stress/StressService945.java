package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService945 {
    public String performTask945() {
        return "Task 945 result";
    }
    
    public void crossCall(StressService946 other) {
        other.performTask946();
    }
}
