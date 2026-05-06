package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService904 {
    public String performTask904() {
        return "Task 904 result";
    }
    
    public void crossCall(StressService905 other) {
        other.performTask905();
    }
}
