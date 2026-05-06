package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService275 {
    public String performTask275() {
        return "Task 275 result";
    }
    
    public void crossCall(StressService276 other) {
        other.performTask276();
    }
}
