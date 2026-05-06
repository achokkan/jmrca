package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService973 {
    public String performTask973() {
        return "Task 973 result";
    }
    
    public void crossCall(StressService974 other) {
        other.performTask974();
    }
}
