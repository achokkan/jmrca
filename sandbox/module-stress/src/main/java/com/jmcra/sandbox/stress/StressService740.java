package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService740 {
    public String performTask740() {
        return "Task 740 result";
    }
    
    public void crossCall(StressService741 other) {
        other.performTask741();
    }
}
