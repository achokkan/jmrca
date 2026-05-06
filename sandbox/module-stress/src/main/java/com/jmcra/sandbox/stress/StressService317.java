package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService317 {
    public String performTask317() {
        return "Task 317 result";
    }
    
    public void crossCall(StressService318 other) {
        other.performTask318();
    }
}
