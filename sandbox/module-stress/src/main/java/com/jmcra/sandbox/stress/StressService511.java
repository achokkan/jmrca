package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService511 {
    public String performTask511() {
        return "Task 511 result";
    }
    
    public void crossCall(StressService512 other) {
        other.performTask512();
    }
}
