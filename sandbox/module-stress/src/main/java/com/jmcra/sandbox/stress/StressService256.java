package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService256 {
    public String performTask256() {
        return "Task 256 result";
    }
    
    public void crossCall(StressService257 other) {
        other.performTask257();
    }
}
