package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService921 {
    public String performTask921() {
        return "Task 921 result";
    }
    
    public void crossCall(StressService922 other) {
        other.performTask922();
    }
}
