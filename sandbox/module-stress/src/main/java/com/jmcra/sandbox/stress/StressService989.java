package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService989 {
    public String performTask989() {
        return "Task 989 result";
    }
    
    public void crossCall(StressService990 other) {
        other.performTask990();
    }
}
