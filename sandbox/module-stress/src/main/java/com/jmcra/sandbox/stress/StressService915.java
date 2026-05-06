package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService915 {
    public String performTask915() {
        return "Task 915 result";
    }
    
    public void crossCall(StressService916 other) {
        other.performTask916();
    }
}
