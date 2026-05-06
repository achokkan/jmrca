package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService625 {
    public String performTask625() {
        return "Task 625 result";
    }
    
    public void crossCall(StressService626 other) {
        other.performTask626();
    }
}
