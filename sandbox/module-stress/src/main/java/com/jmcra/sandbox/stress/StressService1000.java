package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService1000 {
    public String performTask1000() {
        return "Task 1000 result";
    }
    
    public void crossCall(StressService1 other) {
        other.performTask1();
    }
}
