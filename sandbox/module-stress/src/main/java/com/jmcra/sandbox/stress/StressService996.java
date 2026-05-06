package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService996 {
    public String performTask996() {
        return "Task 996 result";
    }
    
    public void crossCall(StressService997 other) {
        other.performTask997();
    }
}
