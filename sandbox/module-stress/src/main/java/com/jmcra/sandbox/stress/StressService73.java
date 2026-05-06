package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService73 {
    public String performTask73() {
        return "Task 73 result";
    }
    
    public void crossCall(StressService74 other) {
        other.performTask74();
    }
}
