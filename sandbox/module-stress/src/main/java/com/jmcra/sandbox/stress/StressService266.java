package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService266 {
    public String performTask266() {
        return "Task 266 result";
    }
    
    public void crossCall(StressService267 other) {
        other.performTask267();
    }
}
