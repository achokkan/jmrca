package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService571 {
    public String performTask571() {
        return "Task 571 result";
    }
    
    public void crossCall(StressService572 other) {
        other.performTask572();
    }
}
