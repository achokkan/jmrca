package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService605 {
    public String performTask605() {
        return "Task 605 result";
    }
    
    public void crossCall(StressService606 other) {
        other.performTask606();
    }
}
