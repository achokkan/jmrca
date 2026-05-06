package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService301 {
    public String performTask301() {
        return "Task 301 result";
    }
    
    public void crossCall(StressService302 other) {
        other.performTask302();
    }
}
