package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService330 {
    public String performTask330() {
        return "Task 330 result";
    }
    
    public void crossCall(StressService331 other) {
        other.performTask331();
    }
}
