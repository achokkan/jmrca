package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService105 {
    public String performTask105() {
        return "Task 105 result";
    }
    
    public void crossCall(StressService106 other) {
        other.performTask106();
    }
}
