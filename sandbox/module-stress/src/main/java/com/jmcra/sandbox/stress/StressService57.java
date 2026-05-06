package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService57 {
    public String performTask57() {
        return "Task 57 result";
    }
    
    public void crossCall(StressService58 other) {
        other.performTask58();
    }
}
