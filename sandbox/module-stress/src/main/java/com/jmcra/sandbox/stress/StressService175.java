package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService175 {
    public String performTask175() {
        return "Task 175 result";
    }
    
    public void crossCall(StressService176 other) {
        other.performTask176();
    }
}
