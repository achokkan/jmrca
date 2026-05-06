package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService859 {
    public String performTask859() {
        return "Task 859 result";
    }
    
    public void crossCall(StressService860 other) {
        other.performTask860();
    }
}
