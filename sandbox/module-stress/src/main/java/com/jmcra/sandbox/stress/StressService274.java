package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService274 {
    public String performTask274() {
        return "Task 274 result";
    }
    
    public void crossCall(StressService275 other) {
        other.performTask275();
    }
}
