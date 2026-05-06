package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService423 {
    public String performTask423() {
        return "Task 423 result";
    }
    
    public void crossCall(StressService424 other) {
        other.performTask424();
    }
}
