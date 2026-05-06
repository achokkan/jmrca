package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService13 {
    public String performTask13() {
        return "Task 13 result";
    }
    
    public void crossCall(StressService14 other) {
        other.performTask14();
    }
}
