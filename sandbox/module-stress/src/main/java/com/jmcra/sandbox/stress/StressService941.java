package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService941 {
    public String performTask941() {
        return "Task 941 result";
    }
    
    public void crossCall(StressService942 other) {
        other.performTask942();
    }
}
