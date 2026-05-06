package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService51 {
    public String performTask51() {
        return "Task 51 result";
    }
    
    public void crossCall(StressService52 other) {
        other.performTask52();
    }
}
