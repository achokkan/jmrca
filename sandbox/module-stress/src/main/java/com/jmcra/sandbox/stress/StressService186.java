package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService186 {
    public String performTask186() {
        return "Task 186 result";
    }
    
    public void crossCall(StressService187 other) {
        other.performTask187();
    }
}
