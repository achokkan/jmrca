package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService980 {
    public String performTask980() {
        return "Task 980 result";
    }
    
    public void crossCall(StressService981 other) {
        other.performTask981();
    }
}
