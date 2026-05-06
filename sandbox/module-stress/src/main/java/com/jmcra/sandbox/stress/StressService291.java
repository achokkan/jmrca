package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService291 {
    public String performTask291() {
        return "Task 291 result";
    }
    
    public void crossCall(StressService292 other) {
        other.performTask292();
    }
}
