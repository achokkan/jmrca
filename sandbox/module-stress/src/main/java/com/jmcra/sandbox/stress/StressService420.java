package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService420 {
    public String performTask420() {
        return "Task 420 result";
    }
    
    public void crossCall(StressService421 other) {
        other.performTask421();
    }
}
