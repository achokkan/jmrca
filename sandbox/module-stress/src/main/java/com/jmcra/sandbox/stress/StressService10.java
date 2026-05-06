package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService10 {
    public String performTask10() {
        return "Task 10 result";
    }
    
    public void crossCall(StressService11 other) {
        other.performTask11();
    }
}
