package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService187 {
    public String performTask187() {
        return "Task 187 result";
    }
    
    public void crossCall(StressService188 other) {
        other.performTask188();
    }
}
