package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService207 {
    public String performTask207() {
        return "Task 207 result";
    }
    
    public void crossCall(StressService208 other) {
        other.performTask208();
    }
}
