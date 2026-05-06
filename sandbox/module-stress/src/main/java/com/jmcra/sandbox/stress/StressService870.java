package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService870 {
    public String performTask870() {
        return "Task 870 result";
    }
    
    public void crossCall(StressService871 other) {
        other.performTask871();
    }
}
