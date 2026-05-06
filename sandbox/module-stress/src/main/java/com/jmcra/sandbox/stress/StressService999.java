package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService999 {
    public String performTask999() {
        return "Task 999 result";
    }
    
    public void crossCall(StressService1000 other) {
        other.performTask1000();
    }
}
