package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService111 {
    public String performTask111() {
        return "Task 111 result";
    }
    
    public void crossCall(StressService112 other) {
        other.performTask112();
    }
}
