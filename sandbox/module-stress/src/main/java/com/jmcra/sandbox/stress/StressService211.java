package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService211 {
    public String performTask211() {
        return "Task 211 result";
    }
    
    public void crossCall(StressService212 other) {
        other.performTask212();
    }
}
