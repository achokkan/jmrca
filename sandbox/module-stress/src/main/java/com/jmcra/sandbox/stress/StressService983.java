package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService983 {
    public String performTask983() {
        return "Task 983 result";
    }
    
    public void crossCall(StressService984 other) {
        other.performTask984();
    }
}
