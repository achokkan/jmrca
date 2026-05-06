package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService122 {
    public String performTask122() {
        return "Task 122 result";
    }
    
    public void crossCall(StressService123 other) {
        other.performTask123();
    }
}
