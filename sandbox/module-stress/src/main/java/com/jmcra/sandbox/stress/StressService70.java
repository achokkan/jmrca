package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService70 {
    public String performTask70() {
        return "Task 70 result";
    }
    
    public void crossCall(StressService71 other) {
        other.performTask71();
    }
}
