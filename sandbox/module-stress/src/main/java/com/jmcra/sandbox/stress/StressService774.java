package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService774 {
    public String performTask774() {
        return "Task 774 result";
    }
    
    public void crossCall(StressService775 other) {
        other.performTask775();
    }
}
