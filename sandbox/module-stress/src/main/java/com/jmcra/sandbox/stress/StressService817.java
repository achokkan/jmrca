package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService817 {
    public String performTask817() {
        return "Task 817 result";
    }
    
    public void crossCall(StressService818 other) {
        other.performTask818();
    }
}
