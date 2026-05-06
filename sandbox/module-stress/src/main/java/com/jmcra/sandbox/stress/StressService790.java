package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService790 {
    public String performTask790() {
        return "Task 790 result";
    }
    
    public void crossCall(StressService791 other) {
        other.performTask791();
    }
}
