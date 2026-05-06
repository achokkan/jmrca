package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService856 {
    public String performTask856() {
        return "Task 856 result";
    }
    
    public void crossCall(StressService857 other) {
        other.performTask857();
    }
}
