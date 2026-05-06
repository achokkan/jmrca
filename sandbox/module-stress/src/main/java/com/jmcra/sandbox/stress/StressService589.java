package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService589 {
    public String performTask589() {
        return "Task 589 result";
    }
    
    public void crossCall(StressService590 other) {
        other.performTask590();
    }
}
