package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService964 {
    public String performTask964() {
        return "Task 964 result";
    }
    
    public void crossCall(StressService965 other) {
        other.performTask965();
    }
}
