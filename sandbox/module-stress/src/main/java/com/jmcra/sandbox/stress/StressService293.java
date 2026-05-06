package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService293 {
    public String performTask293() {
        return "Task 293 result";
    }
    
    public void crossCall(StressService294 other) {
        other.performTask294();
    }
}
