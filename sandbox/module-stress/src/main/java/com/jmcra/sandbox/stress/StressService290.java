package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService290 {
    public String performTask290() {
        return "Task 290 result";
    }
    
    public void crossCall(StressService291 other) {
        other.performTask291();
    }
}
