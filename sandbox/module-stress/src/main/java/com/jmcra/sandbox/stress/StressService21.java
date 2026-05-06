package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService21 {
    public String performTask21() {
        return "Task 21 result";
    }
    
    public void crossCall(StressService22 other) {
        other.performTask22();
    }
}
