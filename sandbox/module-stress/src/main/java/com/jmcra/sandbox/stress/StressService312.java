package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService312 {
    public String performTask312() {
        return "Task 312 result";
    }
    
    public void crossCall(StressService313 other) {
        other.performTask313();
    }
}
