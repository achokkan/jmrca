package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService407 {
    public String performTask407() {
        return "Task 407 result";
    }
    
    public void crossCall(StressService408 other) {
        other.performTask408();
    }
}
