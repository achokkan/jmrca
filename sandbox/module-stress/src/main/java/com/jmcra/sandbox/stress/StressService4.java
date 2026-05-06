package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService4 {
    public String performTask4() {
        return "Task 4 result";
    }
    
    public void crossCall(StressService5 other) {
        other.performTask5();
    }
}
