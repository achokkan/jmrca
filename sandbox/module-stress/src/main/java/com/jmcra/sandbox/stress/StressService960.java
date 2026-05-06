package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService960 {
    public String performTask960() {
        return "Task 960 result";
    }
    
    public void crossCall(StressService961 other) {
        other.performTask961();
    }
}
