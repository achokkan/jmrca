package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService214 {
    public String performTask214() {
        return "Task 214 result";
    }
    
    public void crossCall(StressService215 other) {
        other.performTask215();
    }
}
