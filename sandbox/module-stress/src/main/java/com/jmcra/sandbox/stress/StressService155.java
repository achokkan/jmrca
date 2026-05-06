package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService155 {
    public String performTask155() {
        return "Task 155 result";
    }
    
    public void crossCall(StressService156 other) {
        other.performTask156();
    }
}
