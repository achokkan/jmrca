package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService117 {
    public String performTask117() {
        return "Task 117 result";
    }
    
    public void crossCall(StressService118 other) {
        other.performTask118();
    }
}
