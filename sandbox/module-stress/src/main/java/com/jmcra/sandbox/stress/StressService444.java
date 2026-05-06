package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService444 {
    public String performTask444() {
        return "Task 444 result";
    }
    
    public void crossCall(StressService445 other) {
        other.performTask445();
    }
}
