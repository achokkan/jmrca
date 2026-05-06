package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService720 {
    public String performTask720() {
        return "Task 720 result";
    }
    
    public void crossCall(StressService721 other) {
        other.performTask721();
    }
}
