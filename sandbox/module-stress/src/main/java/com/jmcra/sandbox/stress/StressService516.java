package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService516 {
    public String performTask516() {
        return "Task 516 result";
    }
    
    public void crossCall(StressService517 other) {
        other.performTask517();
    }
}
