package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService178 {
    public String performTask178() {
        return "Task 178 result";
    }
    
    public void crossCall(StressService179 other) {
        other.performTask179();
    }
}
