package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService563 {
    public String performTask563() {
        return "Task 563 result";
    }
    
    public void crossCall(StressService564 other) {
        other.performTask564();
    }
}
