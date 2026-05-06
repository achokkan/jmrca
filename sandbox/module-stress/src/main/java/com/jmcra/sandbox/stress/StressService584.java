package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService584 {
    public String performTask584() {
        return "Task 584 result";
    }
    
    public void crossCall(StressService585 other) {
        other.performTask585();
    }
}
