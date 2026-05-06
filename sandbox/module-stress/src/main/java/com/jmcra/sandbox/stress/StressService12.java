package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService12 {
    public String performTask12() {
        return "Task 12 result";
    }
    
    public void crossCall(StressService13 other) {
        other.performTask13();
    }
}
