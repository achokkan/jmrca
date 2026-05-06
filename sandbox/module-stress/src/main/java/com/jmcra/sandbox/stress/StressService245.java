package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService245 {
    public String performTask245() {
        return "Task 245 result";
    }
    
    public void crossCall(StressService246 other) {
        other.performTask246();
    }
}
