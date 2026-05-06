package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService376 {
    public String performTask376() {
        return "Task 376 result";
    }
    
    public void crossCall(StressService377 other) {
        other.performTask377();
    }
}
