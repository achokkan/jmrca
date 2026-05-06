package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService273 {
    public String performTask273() {
        return "Task 273 result";
    }
    
    public void crossCall(StressService274 other) {
        other.performTask274();
    }
}
