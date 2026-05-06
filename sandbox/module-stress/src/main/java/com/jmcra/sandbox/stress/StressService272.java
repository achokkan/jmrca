package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService272 {
    public String performTask272() {
        return "Task 272 result";
    }
    
    public void crossCall(StressService273 other) {
        other.performTask273();
    }
}
