package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService102 {
    public String performTask102() {
        return "Task 102 result";
    }
    
    public void crossCall(StressService103 other) {
        other.performTask103();
    }
}
