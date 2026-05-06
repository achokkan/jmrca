package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService587 {
    public String performTask587() {
        return "Task 587 result";
    }
    
    public void crossCall(StressService588 other) {
        other.performTask588();
    }
}
