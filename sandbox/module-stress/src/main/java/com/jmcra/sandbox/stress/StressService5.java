package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService5 {
    public String performTask5() {
        return "Task 5 result";
    }
    
    public void crossCall(StressService6 other) {
        other.performTask6();
    }
}
