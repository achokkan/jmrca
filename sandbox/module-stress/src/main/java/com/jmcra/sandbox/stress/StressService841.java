package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService841 {
    public String performTask841() {
        return "Task 841 result";
    }
    
    public void crossCall(StressService842 other) {
        other.performTask842();
    }
}
