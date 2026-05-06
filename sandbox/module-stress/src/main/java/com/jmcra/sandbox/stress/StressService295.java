package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService295 {
    public String performTask295() {
        return "Task 295 result";
    }
    
    public void crossCall(StressService296 other) {
        other.performTask296();
    }
}
