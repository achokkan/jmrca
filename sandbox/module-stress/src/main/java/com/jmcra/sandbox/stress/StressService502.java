package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService502 {
    public String performTask502() {
        return "Task 502 result";
    }
    
    public void crossCall(StressService503 other) {
        other.performTask503();
    }
}
