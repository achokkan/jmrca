package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService922 {
    public String performTask922() {
        return "Task 922 result";
    }
    
    public void crossCall(StressService923 other) {
        other.performTask923();
    }
}
