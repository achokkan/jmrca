package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService340 {
    public String performTask340() {
        return "Task 340 result";
    }
    
    public void crossCall(StressService341 other) {
        other.performTask341();
    }
}
