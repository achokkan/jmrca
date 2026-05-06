package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService2 {
    public String performTask2() {
        return "Task 2 result";
    }
    
    public void crossCall(StressService3 other) {
        other.performTask3();
    }
}
