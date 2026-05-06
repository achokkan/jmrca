package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService77 {
    public String performTask77() {
        return "Task 77 result";
    }
    
    public void crossCall(StressService78 other) {
        other.performTask78();
    }
}
