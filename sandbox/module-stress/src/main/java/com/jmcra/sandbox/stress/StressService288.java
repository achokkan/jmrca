package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService288 {
    public String performTask288() {
        return "Task 288 result";
    }
    
    public void crossCall(StressService289 other) {
        other.performTask289();
    }
}
