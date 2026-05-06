package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService981 {
    public String performTask981() {
        return "Task 981 result";
    }
    
    public void crossCall(StressService982 other) {
        other.performTask982();
    }
}
