package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService874 {
    public String performTask874() {
        return "Task 874 result";
    }
    
    public void crossCall(StressService875 other) {
        other.performTask875();
    }
}
