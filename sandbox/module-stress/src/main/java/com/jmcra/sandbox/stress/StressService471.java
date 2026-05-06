package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService471 {
    public String performTask471() {
        return "Task 471 result";
    }
    
    public void crossCall(StressService472 other) {
        other.performTask472();
    }
}
