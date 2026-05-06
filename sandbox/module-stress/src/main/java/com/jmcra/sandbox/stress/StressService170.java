package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService170 {
    public String performTask170() {
        return "Task 170 result";
    }
    
    public void crossCall(StressService171 other) {
        other.performTask171();
    }
}
