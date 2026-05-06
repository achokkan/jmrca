package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService522 {
    public String performTask522() {
        return "Task 522 result";
    }
    
    public void crossCall(StressService523 other) {
        other.performTask523();
    }
}
