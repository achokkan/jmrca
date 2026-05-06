package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService899 {
    public String performTask899() {
        return "Task 899 result";
    }
    
    public void crossCall(StressService900 other) {
        other.performTask900();
    }
}
