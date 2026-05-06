package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService40 {
    public String performTask40() {
        return "Task 40 result";
    }
    
    public void crossCall(StressService41 other) {
        other.performTask41();
    }
}
