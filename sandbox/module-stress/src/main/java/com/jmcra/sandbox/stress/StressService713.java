package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService713 {
    public String performTask713() {
        return "Task 713 result";
    }
    
    public void crossCall(StressService714 other) {
        other.performTask714();
    }
}
