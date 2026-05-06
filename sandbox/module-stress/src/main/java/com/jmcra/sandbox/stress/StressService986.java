package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService986 {
    public String performTask986() {
        return "Task 986 result";
    }
    
    public void crossCall(StressService987 other) {
        other.performTask987();
    }
}
