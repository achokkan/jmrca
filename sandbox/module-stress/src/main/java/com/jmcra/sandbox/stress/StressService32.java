package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService32 {
    public String performTask32() {
        return "Task 32 result";
    }
    
    public void crossCall(StressService33 other) {
        other.performTask33();
    }
}
