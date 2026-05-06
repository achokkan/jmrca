package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService861 {
    public String performTask861() {
        return "Task 861 result";
    }
    
    public void crossCall(StressService862 other) {
        other.performTask862();
    }
}
