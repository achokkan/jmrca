package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService250 {
    public String performTask250() {
        return "Task 250 result";
    }
    
    public void crossCall(StressService251 other) {
        other.performTask251();
    }
}
