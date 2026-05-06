package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService270 {
    public String performTask270() {
        return "Task 270 result";
    }
    
    public void crossCall(StressService271 other) {
        other.performTask271();
    }
}
