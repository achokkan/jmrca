package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService527 {
    public String performTask527() {
        return "Task 527 result";
    }
    
    public void crossCall(StressService528 other) {
        other.performTask528();
    }
}
