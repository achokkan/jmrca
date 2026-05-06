package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService487 {
    public String performTask487() {
        return "Task 487 result";
    }
    
    public void crossCall(StressService488 other) {
        other.performTask488();
    }
}
