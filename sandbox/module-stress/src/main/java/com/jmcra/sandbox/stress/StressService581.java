package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService581 {
    public String performTask581() {
        return "Task 581 result";
    }
    
    public void crossCall(StressService582 other) {
        other.performTask582();
    }
}
