package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService701 {
    public String performTask701() {
        return "Task 701 result";
    }
    
    public void crossCall(StressService702 other) {
        other.performTask702();
    }
}
