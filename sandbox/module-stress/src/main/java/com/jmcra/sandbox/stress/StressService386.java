package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService386 {
    public String performTask386() {
        return "Task 386 result";
    }
    
    public void crossCall(StressService387 other) {
        other.performTask387();
    }
}
