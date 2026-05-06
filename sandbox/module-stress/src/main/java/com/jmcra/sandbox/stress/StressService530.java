package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService530 {
    public String performTask530() {
        return "Task 530 result";
    }
    
    public void crossCall(StressService531 other) {
        other.performTask531();
    }
}
