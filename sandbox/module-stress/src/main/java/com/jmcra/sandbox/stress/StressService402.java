package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService402 {
    public String performTask402() {
        return "Task 402 result";
    }
    
    public void crossCall(StressService403 other) {
        other.performTask403();
    }
}
