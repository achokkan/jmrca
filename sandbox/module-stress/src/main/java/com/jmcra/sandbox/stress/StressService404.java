package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService404 {
    public String performTask404() {
        return "Task 404 result";
    }
    
    public void crossCall(StressService405 other) {
        other.performTask405();
    }
}
