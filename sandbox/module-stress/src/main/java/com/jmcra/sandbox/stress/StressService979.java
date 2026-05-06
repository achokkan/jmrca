package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService979 {
    public String performTask979() {
        return "Task 979 result";
    }
    
    public void crossCall(StressService980 other) {
        other.performTask980();
    }
}
