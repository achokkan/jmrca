package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService606 {
    public String performTask606() {
        return "Task 606 result";
    }
    
    public void crossCall(StressService607 other) {
        other.performTask607();
    }
}
