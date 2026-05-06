package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService378 {
    public String performTask378() {
        return "Task 378 result";
    }
    
    public void crossCall(StressService379 other) {
        other.performTask379();
    }
}
