package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService377 {
    public String performTask377() {
        return "Task 377 result";
    }
    
    public void crossCall(StressService378 other) {
        other.performTask378();
    }
}
