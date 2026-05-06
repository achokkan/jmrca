package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService22 {
    public String performTask22() {
        return "Task 22 result";
    }
    
    public void crossCall(StressService23 other) {
        other.performTask23();
    }
}
