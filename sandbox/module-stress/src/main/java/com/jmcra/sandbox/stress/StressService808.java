package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService808 {
    public String performTask808() {
        return "Task 808 result";
    }
    
    public void crossCall(StressService809 other) {
        other.performTask809();
    }
}
