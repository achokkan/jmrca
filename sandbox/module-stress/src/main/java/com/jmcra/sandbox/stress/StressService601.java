package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService601 {
    public String performTask601() {
        return "Task 601 result";
    }
    
    public void crossCall(StressService602 other) {
        other.performTask602();
    }
}
