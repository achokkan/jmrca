package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService230 {
    public String performTask230() {
        return "Task 230 result";
    }
    
    public void crossCall(StressService231 other) {
        other.performTask231();
    }
}
