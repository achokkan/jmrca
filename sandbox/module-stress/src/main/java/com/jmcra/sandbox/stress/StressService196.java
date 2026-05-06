package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService196 {
    public String performTask196() {
        return "Task 196 result";
    }
    
    public void crossCall(StressService197 other) {
        other.performTask197();
    }
}
