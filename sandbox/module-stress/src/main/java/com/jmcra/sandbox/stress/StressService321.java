package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService321 {
    public String performTask321() {
        return "Task 321 result";
    }
    
    public void crossCall(StressService322 other) {
        other.performTask322();
    }
}
