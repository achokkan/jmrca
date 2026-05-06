package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService419 {
    public String performTask419() {
        return "Task 419 result";
    }
    
    public void crossCall(StressService420 other) {
        other.performTask420();
    }
}
