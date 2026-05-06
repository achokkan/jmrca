package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService600 {
    public String performTask600() {
        return "Task 600 result";
    }
    
    public void crossCall(StressService601 other) {
        other.performTask601();
    }
}
