package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService814 {
    public String performTask814() {
        return "Task 814 result";
    }
    
    public void crossCall(StressService815 other) {
        other.performTask815();
    }
}
