package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService124 {
    public String performTask124() {
        return "Task 124 result";
    }
    
    public void crossCall(StressService125 other) {
        other.performTask125();
    }
}
