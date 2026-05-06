package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService309 {
    public String performTask309() {
        return "Task 309 result";
    }
    
    public void crossCall(StressService310 other) {
        other.performTask310();
    }
}
