package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService993 {
    public String performTask993() {
        return "Task 993 result";
    }
    
    public void crossCall(StressService994 other) {
        other.performTask994();
    }
}
