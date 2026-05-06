package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService974 {
    public String performTask974() {
        return "Task 974 result";
    }
    
    public void crossCall(StressService975 other) {
        other.performTask975();
    }
}
