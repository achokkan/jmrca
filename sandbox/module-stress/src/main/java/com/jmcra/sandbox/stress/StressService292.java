package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService292 {
    public String performTask292() {
        return "Task 292 result";
    }
    
    public void crossCall(StressService293 other) {
        other.performTask293();
    }
}
