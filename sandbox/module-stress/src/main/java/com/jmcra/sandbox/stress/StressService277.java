package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService277 {
    public String performTask277() {
        return "Task 277 result";
    }
    
    public void crossCall(StressService278 other) {
        other.performTask278();
    }
}
