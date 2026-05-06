package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService332 {
    public String performTask332() {
        return "Task 332 result";
    }
    
    public void crossCall(StressService333 other) {
        other.performTask333();
    }
}
