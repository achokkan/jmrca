package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService215 {
    public String performTask215() {
        return "Task 215 result";
    }
    
    public void crossCall(StressService216 other) {
        other.performTask216();
    }
}
