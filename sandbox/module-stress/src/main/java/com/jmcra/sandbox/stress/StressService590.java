package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService590 {
    public String performTask590() {
        return "Task 590 result";
    }
    
    public void crossCall(StressService591 other) {
        other.performTask591();
    }
}
