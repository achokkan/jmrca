package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService860 {
    public String performTask860() {
        return "Task 860 result";
    }
    
    public void crossCall(StressService861 other) {
        other.performTask861();
    }
}
