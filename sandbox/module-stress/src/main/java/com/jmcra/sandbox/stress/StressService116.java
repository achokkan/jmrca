package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService116 {
    public String performTask116() {
        return "Task 116 result";
    }
    
    public void crossCall(StressService117 other) {
        other.performTask117();
    }
}
