package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService497 {
    public String performTask497() {
        return "Task 497 result";
    }
    
    public void crossCall(StressService498 other) {
        other.performTask498();
    }
}
