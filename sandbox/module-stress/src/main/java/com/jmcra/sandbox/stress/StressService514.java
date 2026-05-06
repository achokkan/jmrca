package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService514 {
    public String performTask514() {
        return "Task 514 result";
    }
    
    public void crossCall(StressService515 other) {
        other.performTask515();
    }
}
