package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService387 {
    public String performTask387() {
        return "Task 387 result";
    }
    
    public void crossCall(StressService388 other) {
        other.performTask388();
    }
}
