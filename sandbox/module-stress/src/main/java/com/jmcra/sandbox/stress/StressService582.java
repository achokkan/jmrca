package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService582 {
    public String performTask582() {
        return "Task 582 result";
    }
    
    public void crossCall(StressService583 other) {
        other.performTask583();
    }
}
