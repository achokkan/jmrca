package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService777 {
    public String performTask777() {
        return "Task 777 result";
    }
    
    public void crossCall(StressService778 other) {
        other.performTask778();
    }
}
