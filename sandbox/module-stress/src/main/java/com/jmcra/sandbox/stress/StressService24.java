package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService24 {
    public String performTask24() {
        return "Task 24 result";
    }
    
    public void crossCall(StressService25 other) {
        other.performTask25();
    }
}
