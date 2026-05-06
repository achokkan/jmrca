package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService907 {
    public String performTask907() {
        return "Task 907 result";
    }
    
    public void crossCall(StressService908 other) {
        other.performTask908();
    }
}
