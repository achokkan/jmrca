package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService318 {
    public String performTask318() {
        return "Task 318 result";
    }
    
    public void crossCall(StressService319 other) {
        other.performTask319();
    }
}
