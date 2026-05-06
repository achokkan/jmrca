package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService41 {
    public String performTask41() {
        return "Task 41 result";
    }
    
    public void crossCall(StressService42 other) {
        other.performTask42();
    }
}
