package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService408 {
    public String performTask408() {
        return "Task 408 result";
    }
    
    public void crossCall(StressService409 other) {
        other.performTask409();
    }
}
