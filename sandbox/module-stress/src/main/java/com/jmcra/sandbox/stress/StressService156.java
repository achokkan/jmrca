package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService156 {
    public String performTask156() {
        return "Task 156 result";
    }
    
    public void crossCall(StressService157 other) {
        other.performTask157();
    }
}
