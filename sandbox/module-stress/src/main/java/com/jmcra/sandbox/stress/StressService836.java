package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService836 {
    public String performTask836() {
        return "Task 836 result";
    }
    
    public void crossCall(StressService837 other) {
        other.performTask837();
    }
}
