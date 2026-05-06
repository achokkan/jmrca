package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService561 {
    public String performTask561() {
        return "Task 561 result";
    }
    
    public void crossCall(StressService562 other) {
        other.performTask562();
    }
}
