package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService366 {
    public String performTask366() {
        return "Task 366 result";
    }
    
    public void crossCall(StressService367 other) {
        other.performTask367();
    }
}
