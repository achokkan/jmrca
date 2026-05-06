package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService470 {
    public String performTask470() {
        return "Task 470 result";
    }
    
    public void crossCall(StressService471 other) {
        other.performTask471();
    }
}
