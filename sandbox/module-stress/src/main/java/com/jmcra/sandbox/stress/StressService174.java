package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService174 {
    public String performTask174() {
        return "Task 174 result";
    }
    
    public void crossCall(StressService175 other) {
        other.performTask175();
    }
}
