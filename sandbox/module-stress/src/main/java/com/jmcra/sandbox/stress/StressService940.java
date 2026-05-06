package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService940 {
    public String performTask940() {
        return "Task 940 result";
    }
    
    public void crossCall(StressService941 other) {
        other.performTask941();
    }
}
