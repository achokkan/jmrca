package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService405 {
    public String performTask405() {
        return "Task 405 result";
    }
    
    public void crossCall(StressService406 other) {
        other.performTask406();
    }
}
