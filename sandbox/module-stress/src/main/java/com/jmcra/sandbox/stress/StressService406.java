package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService406 {
    public String performTask406() {
        return "Task 406 result";
    }
    
    public void crossCall(StressService407 other) {
        other.performTask407();
    }
}
