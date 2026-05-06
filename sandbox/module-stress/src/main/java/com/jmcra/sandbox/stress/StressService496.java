package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService496 {
    public String performTask496() {
        return "Task 496 result";
    }
    
    public void crossCall(StressService497 other) {
        other.performTask497();
    }
}
