package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService397 {
    public String performTask397() {
        return "Task 397 result";
    }
    
    public void crossCall(StressService398 other) {
        other.performTask398();
    }
}
