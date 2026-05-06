package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService48 {
    public String performTask48() {
        return "Task 48 result";
    }
    
    public void crossCall(StressService49 other) {
        other.performTask49();
    }
}
