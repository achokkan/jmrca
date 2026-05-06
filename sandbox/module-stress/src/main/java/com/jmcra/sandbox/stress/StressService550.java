package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService550 {
    public String performTask550() {
        return "Task 550 result";
    }
    
    public void crossCall(StressService551 other) {
        other.performTask551();
    }
}
