package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService490 {
    public String performTask490() {
        return "Task 490 result";
    }
    
    public void crossCall(StressService491 other) {
        other.performTask491();
    }
}
