package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService875 {
    public String performTask875() {
        return "Task 875 result";
    }
    
    public void crossCall(StressService876 other) {
        other.performTask876();
    }
}
