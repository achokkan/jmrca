package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService564 {
    public String performTask564() {
        return "Task 564 result";
    }
    
    public void crossCall(StressService565 other) {
        other.performTask565();
    }
}
