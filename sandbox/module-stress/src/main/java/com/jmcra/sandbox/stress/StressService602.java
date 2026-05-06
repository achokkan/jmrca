package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService602 {
    public String performTask602() {
        return "Task 602 result";
    }
    
    public void crossCall(StressService603 other) {
        other.performTask603();
    }
}
