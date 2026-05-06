package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService43 {
    public String performTask43() {
        return "Task 43 result";
    }
    
    public void crossCall(StressService44 other) {
        other.performTask44();
    }
}
