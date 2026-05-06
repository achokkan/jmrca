package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService799 {
    public String performTask799() {
        return "Task 799 result";
    }
    
    public void crossCall(StressService800 other) {
        other.performTask800();
    }
}
