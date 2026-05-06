package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService966 {
    public String performTask966() {
        return "Task 966 result";
    }
    
    public void crossCall(StressService967 other) {
        other.performTask967();
    }
}
