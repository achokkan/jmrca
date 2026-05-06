package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService905 {
    public String performTask905() {
        return "Task 905 result";
    }
    
    public void crossCall(StressService906 other) {
        other.performTask906();
    }
}
