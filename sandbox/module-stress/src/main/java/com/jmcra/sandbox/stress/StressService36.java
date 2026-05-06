package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService36 {
    public String performTask36() {
        return "Task 36 result";
    }
    
    public void crossCall(StressService37 other) {
        other.performTask37();
    }
}
