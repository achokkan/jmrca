package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService971 {
    public String performTask971() {
        return "Task 971 result";
    }
    
    public void crossCall(StressService972 other) {
        other.performTask972();
    }
}
