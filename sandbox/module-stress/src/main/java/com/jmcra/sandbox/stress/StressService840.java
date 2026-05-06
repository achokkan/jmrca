package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService840 {
    public String performTask840() {
        return "Task 840 result";
    }
    
    public void crossCall(StressService841 other) {
        other.performTask841();
    }
}
