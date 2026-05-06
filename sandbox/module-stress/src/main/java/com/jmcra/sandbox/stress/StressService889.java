package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService889 {
    public String performTask889() {
        return "Task 889 result";
    }
    
    public void crossCall(StressService890 other) {
        other.performTask890();
    }
}
