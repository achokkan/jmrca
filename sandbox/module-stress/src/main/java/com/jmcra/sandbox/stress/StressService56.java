package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService56 {
    public String performTask56() {
        return "Task 56 result";
    }
    
    public void crossCall(StressService57 other) {
        other.performTask57();
    }
}
