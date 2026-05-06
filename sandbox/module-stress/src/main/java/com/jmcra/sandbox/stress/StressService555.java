package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService555 {
    public String performTask555() {
        return "Task 555 result";
    }
    
    public void crossCall(StressService556 other) {
        other.performTask556();
    }
}
