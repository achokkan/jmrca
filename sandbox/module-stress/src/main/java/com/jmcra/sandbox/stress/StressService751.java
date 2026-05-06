package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService751 {
    public String performTask751() {
        return "Task 751 result";
    }
    
    public void crossCall(StressService752 other) {
        other.performTask752();
    }
}
