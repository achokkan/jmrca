package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService3 {
    public String performTask3() {
        return "Task 3 result";
    }
    
    public void crossCall(StressService4 other) {
        other.performTask4();
    }
}
