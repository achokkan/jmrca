package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService898 {
    public String performTask898() {
        return "Task 898 result";
    }
    
    public void crossCall(StressService899 other) {
        other.performTask899();
    }
}
