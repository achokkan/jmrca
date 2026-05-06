package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService811 {
    public String performTask811() {
        return "Task 811 result";
    }
    
    public void crossCall(StressService812 other) {
        other.performTask812();
    }
}
