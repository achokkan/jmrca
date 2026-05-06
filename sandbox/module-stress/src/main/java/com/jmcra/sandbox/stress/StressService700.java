package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService700 {
    public String performTask700() {
        return "Task 700 result";
    }
    
    public void crossCall(StressService701 other) {
        other.performTask701();
    }
}
