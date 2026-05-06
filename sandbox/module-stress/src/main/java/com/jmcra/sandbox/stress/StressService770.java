package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService770 {
    public String performTask770() {
        return "Task 770 result";
    }
    
    public void crossCall(StressService771 other) {
        other.performTask771();
    }
}
