package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService74 {
    public String performTask74() {
        return "Task 74 result";
    }
    
    public void crossCall(StressService75 other) {
        other.performTask75();
    }
}
