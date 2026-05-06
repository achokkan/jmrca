package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService1 {
    public String performTask1() {
        return "Task 1 result";
    }
    
    public void crossCall(StressService2 other) {
        other.performTask2();
    }
}
