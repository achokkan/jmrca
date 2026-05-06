package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService650 {
    public String performTask650() {
        return "Task 650 result";
    }
    
    public void crossCall(StressService651 other) {
        other.performTask651();
    }
}
