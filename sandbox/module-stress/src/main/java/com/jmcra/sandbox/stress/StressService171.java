package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService171 {
    public String performTask171() {
        return "Task 171 result";
    }
    
    public void crossCall(StressService172 other) {
        other.performTask172();
    }
}
