package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService520 {
    public String performTask520() {
        return "Task 520 result";
    }
    
    public void crossCall(StressService521 other) {
        other.performTask521();
    }
}
