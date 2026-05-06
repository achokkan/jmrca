package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService707 {
    public String performTask707() {
        return "Task 707 result";
    }
    
    public void crossCall(StressService708 other) {
        other.performTask708();
    }
}
