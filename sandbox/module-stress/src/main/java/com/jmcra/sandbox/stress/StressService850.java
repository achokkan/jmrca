package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService850 {
    public String performTask850() {
        return "Task 850 result";
    }
    
    public void crossCall(StressService851 other) {
        other.performTask851();
    }
}
