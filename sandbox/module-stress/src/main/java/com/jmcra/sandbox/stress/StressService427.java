package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService427 {
    public String performTask427() {
        return "Task 427 result";
    }
    
    public void crossCall(StressService428 other) {
        other.performTask428();
    }
}
