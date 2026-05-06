package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService137 {
    public String performTask137() {
        return "Task 137 result";
    }
    
    public void crossCall(StressService138 other) {
        other.performTask138();
    }
}
