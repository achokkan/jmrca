package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService722 {
    public String performTask722() {
        return "Task 722 result";
    }
    
    public void crossCall(StressService723 other) {
        other.performTask723();
    }
}
