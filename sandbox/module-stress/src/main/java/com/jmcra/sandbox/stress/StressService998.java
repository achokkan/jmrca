package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService998 {
    public String performTask998() {
        return "Task 998 result";
    }
    
    public void crossCall(StressService999 other) {
        other.performTask999();
    }
}
