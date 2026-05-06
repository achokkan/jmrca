package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService982 {
    public String performTask982() {
        return "Task 982 result";
    }
    
    public void crossCall(StressService983 other) {
        other.performTask983();
    }
}
