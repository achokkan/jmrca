package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService566 {
    public String performTask566() {
        return "Task 566 result";
    }
    
    public void crossCall(StressService567 other) {
        other.performTask567();
    }
}
