package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService412 {
    public String performTask412() {
        return "Task 412 result";
    }
    
    public void crossCall(StressService413 other) {
        other.performTask413();
    }
}
