package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService923 {
    public String performTask923() {
        return "Task 923 result";
    }
    
    public void crossCall(StressService924 other) {
        other.performTask924();
    }
}
