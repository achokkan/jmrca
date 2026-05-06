package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService985 {
    public String performTask985() {
        return "Task 985 result";
    }
    
    public void crossCall(StressService986 other) {
        other.performTask986();
    }
}
