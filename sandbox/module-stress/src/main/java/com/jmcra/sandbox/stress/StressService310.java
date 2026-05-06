package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService310 {
    public String performTask310() {
        return "Task 310 result";
    }
    
    public void crossCall(StressService311 other) {
        other.performTask311();
    }
}
