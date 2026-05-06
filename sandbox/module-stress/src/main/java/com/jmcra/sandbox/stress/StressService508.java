package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService508 {
    public String performTask508() {
        return "Task 508 result";
    }
    
    public void crossCall(StressService509 other) {
        other.performTask509();
    }
}
