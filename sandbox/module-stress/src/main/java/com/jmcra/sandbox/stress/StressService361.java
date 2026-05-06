package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService361 {
    public String performTask361() {
        return "Task 361 result";
    }
    
    public void crossCall(StressService362 other) {
        other.performTask362();
    }
}
