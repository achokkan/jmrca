package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService525 {
    public String performTask525() {
        return "Task 525 result";
    }
    
    public void crossCall(StressService526 other) {
        other.performTask526();
    }
}
