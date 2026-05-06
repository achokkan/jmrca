package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService190 {
    public String performTask190() {
        return "Task 190 result";
    }
    
    public void crossCall(StressService191 other) {
        other.performTask191();
    }
}
