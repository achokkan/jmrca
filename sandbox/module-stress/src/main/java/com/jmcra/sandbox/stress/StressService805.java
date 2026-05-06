package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService805 {
    public String performTask805() {
        return "Task 805 result";
    }
    
    public void crossCall(StressService806 other) {
        other.performTask806();
    }
}
