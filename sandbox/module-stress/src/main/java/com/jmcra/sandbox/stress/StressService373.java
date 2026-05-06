package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService373 {
    public String performTask373() {
        return "Task 373 result";
    }
    
    public void crossCall(StressService374 other) {
        other.performTask374();
    }
}
