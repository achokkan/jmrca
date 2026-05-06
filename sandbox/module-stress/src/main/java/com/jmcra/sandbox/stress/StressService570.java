package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService570 {
    public String performTask570() {
        return "Task 570 result";
    }
    
    public void crossCall(StressService571 other) {
        other.performTask571();
    }
}
