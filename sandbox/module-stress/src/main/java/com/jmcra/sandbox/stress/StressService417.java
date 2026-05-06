package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService417 {
    public String performTask417() {
        return "Task 417 result";
    }
    
    public void crossCall(StressService418 other) {
        other.performTask418();
    }
}
