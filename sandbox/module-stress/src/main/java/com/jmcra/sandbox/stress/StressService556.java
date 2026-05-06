package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService556 {
    public String performTask556() {
        return "Task 556 result";
    }
    
    public void crossCall(StressService557 other) {
        other.performTask557();
    }
}
