package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService391 {
    public String performTask391() {
        return "Task 391 result";
    }
    
    public void crossCall(StressService392 other) {
        other.performTask392();
    }
}
