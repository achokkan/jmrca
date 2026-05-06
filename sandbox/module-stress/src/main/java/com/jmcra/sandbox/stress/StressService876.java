package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService876 {
    public String performTask876() {
        return "Task 876 result";
    }
    
    public void crossCall(StressService877 other) {
        other.performTask877();
    }
}
