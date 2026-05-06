package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService188 {
    public String performTask188() {
        return "Task 188 result";
    }
    
    public void crossCall(StressService189 other) {
        other.performTask189();
    }
}
