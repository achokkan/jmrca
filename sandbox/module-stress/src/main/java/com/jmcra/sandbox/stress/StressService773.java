package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService773 {
    public String performTask773() {
        return "Task 773 result";
    }
    
    public void crossCall(StressService774 other) {
        other.performTask774();
    }
}
