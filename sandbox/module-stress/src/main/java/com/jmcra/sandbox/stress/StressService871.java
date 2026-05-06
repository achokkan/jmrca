package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService871 {
    public String performTask871() {
        return "Task 871 result";
    }
    
    public void crossCall(StressService872 other) {
        other.performTask872();
    }
}
