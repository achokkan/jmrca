package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService334 {
    public String performTask334() {
        return "Task 334 result";
    }
    
    public void crossCall(StressService335 other) {
        other.performTask335();
    }
}
