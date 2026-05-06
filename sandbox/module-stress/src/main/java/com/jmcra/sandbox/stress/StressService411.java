package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService411 {
    public String performTask411() {
        return "Task 411 result";
    }
    
    public void crossCall(StressService412 other) {
        other.performTask412();
    }
}
