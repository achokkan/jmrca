package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService975 {
    public String performTask975() {
        return "Task 975 result";
    }
    
    public void crossCall(StressService976 other) {
        other.performTask976();
    }
}
