package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService851 {
    public String performTask851() {
        return "Task 851 result";
    }
    
    public void crossCall(StressService852 other) {
        other.performTask852();
    }
}
