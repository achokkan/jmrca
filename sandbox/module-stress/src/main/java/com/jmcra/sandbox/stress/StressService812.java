package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService812 {
    public String performTask812() {
        return "Task 812 result";
    }
    
    public void crossCall(StressService813 other) {
        other.performTask813();
    }
}
