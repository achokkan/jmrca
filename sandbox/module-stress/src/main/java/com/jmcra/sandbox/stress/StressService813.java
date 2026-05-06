package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService813 {
    public String performTask813() {
        return "Task 813 result";
    }
    
    public void crossCall(StressService814 other) {
        other.performTask814();
    }
}
