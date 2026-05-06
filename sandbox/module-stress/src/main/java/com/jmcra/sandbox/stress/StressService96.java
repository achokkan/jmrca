package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService96 {
    public String performTask96() {
        return "Task 96 result";
    }
    
    public void crossCall(StressService97 other) {
        other.performTask97();
    }
}
