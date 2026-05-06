package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService473 {
    public String performTask473() {
        return "Task 473 result";
    }
    
    public void crossCall(StressService474 other) {
        other.performTask474();
    }
}
