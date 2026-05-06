package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService712 {
    public String performTask712() {
        return "Task 712 result";
    }
    
    public void crossCall(StressService713 other) {
        other.performTask713();
    }
}
