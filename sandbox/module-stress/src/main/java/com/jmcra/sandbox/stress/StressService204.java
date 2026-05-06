package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService204 {
    public String performTask204() {
        return "Task 204 result";
    }
    
    public void crossCall(StressService205 other) {
        other.performTask205();
    }
}
