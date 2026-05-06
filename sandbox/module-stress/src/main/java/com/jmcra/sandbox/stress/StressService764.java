package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService764 {
    public String performTask764() {
        return "Task 764 result";
    }
    
    public void crossCall(StressService765 other) {
        other.performTask765();
    }
}
