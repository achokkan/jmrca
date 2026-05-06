package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService572 {
    public String performTask572() {
        return "Task 572 result";
    }
    
    public void crossCall(StressService573 other) {
        other.performTask573();
    }
}
