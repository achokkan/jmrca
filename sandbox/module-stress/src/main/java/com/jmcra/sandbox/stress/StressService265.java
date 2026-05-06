package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService265 {
    public String performTask265() {
        return "Task 265 result";
    }
    
    public void crossCall(StressService266 other) {
        other.performTask266();
    }
}
