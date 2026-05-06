package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService823 {
    public String performTask823() {
        return "Task 823 result";
    }
    
    public void crossCall(StressService824 other) {
        other.performTask824();
    }
}
