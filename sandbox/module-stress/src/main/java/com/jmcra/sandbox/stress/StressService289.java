package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService289 {
    public String performTask289() {
        return "Task 289 result";
    }
    
    public void crossCall(StressService290 other) {
        other.performTask290();
    }
}
