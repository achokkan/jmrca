package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService123 {
    public String performTask123() {
        return "Task 123 result";
    }
    
    public void crossCall(StressService124 other) {
        other.performTask124();
    }
}
