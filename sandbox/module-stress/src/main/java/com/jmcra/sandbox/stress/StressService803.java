package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService803 {
    public String performTask803() {
        return "Task 803 result";
    }
    
    public void crossCall(StressService804 other) {
        other.performTask804();
    }
}
