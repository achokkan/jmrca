package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService741 {
    public String performTask741() {
        return "Task 741 result";
    }
    
    public void crossCall(StressService742 other) {
        other.performTask742();
    }
}
