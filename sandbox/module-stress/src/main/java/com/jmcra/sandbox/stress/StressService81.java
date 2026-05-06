package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService81 {
    public String performTask81() {
        return "Task 81 result";
    }
    
    public void crossCall(StressService82 other) {
        other.performTask82();
    }
}
