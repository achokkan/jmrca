package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService804 {
    public String performTask804() {
        return "Task 804 result";
    }
    
    public void crossCall(StressService805 other) {
        other.performTask805();
    }
}
