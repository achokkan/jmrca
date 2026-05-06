package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService430 {
    public String performTask430() {
        return "Task 430 result";
    }
    
    public void crossCall(StressService431 other) {
        other.performTask431();
    }
}
