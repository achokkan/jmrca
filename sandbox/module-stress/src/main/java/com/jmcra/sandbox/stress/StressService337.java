package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService337 {
    public String performTask337() {
        return "Task 337 result";
    }
    
    public void crossCall(StressService338 other) {
        other.performTask338();
    }
}
