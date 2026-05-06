package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService521 {
    public String performTask521() {
        return "Task 521 result";
    }
    
    public void crossCall(StressService522 other) {
        other.performTask522();
    }
}
