package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService984 {
    public String performTask984() {
        return "Task 984 result";
    }
    
    public void crossCall(StressService985 other) {
        other.performTask985();
    }
}
