package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService313 {
    public String performTask313() {
        return "Task 313 result";
    }
    
    public void crossCall(StressService314 other) {
        other.performTask314();
    }
}
