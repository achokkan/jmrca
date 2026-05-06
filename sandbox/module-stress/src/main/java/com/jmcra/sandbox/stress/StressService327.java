package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService327 {
    public String performTask327() {
        return "Task 327 result";
    }
    
    public void crossCall(StressService328 other) {
        other.performTask328();
    }
}
