package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService210 {
    public String performTask210() {
        return "Task 210 result";
    }
    
    public void crossCall(StressService211 other) {
        other.performTask211();
    }
}
