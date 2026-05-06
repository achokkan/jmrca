package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService31 {
    public String performTask31() {
        return "Task 31 result";
    }
    
    public void crossCall(StressService32 other) {
        other.performTask32();
    }
}
