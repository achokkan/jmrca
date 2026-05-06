package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService335 {
    public String performTask335() {
        return "Task 335 result";
    }
    
    public void crossCall(StressService336 other) {
        other.performTask336();
    }
}
