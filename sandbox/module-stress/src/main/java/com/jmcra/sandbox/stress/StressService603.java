package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService603 {
    public String performTask603() {
        return "Task 603 result";
    }
    
    public void crossCall(StressService604 other) {
        other.performTask604();
    }
}
