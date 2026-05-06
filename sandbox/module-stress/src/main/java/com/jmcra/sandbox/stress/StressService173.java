package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService173 {
    public String performTask173() {
        return "Task 173 result";
    }
    
    public void crossCall(StressService174 other) {
        other.performTask174();
    }
}
