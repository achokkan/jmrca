package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService108 {
    public String performTask108() {
        return "Task 108 result";
    }
    
    public void crossCall(StressService109 other) {
        other.performTask109();
    }
}
