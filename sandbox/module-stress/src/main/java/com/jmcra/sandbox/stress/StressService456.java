package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService456 {
    public String performTask456() {
        return "Task 456 result";
    }
    
    public void crossCall(StressService457 other) {
        other.performTask457();
    }
}
