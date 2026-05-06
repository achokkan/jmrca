package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService150 {
    public String performTask150() {
        return "Task 150 result";
    }
    
    public void crossCall(StressService151 other) {
        other.performTask151();
    }
}
