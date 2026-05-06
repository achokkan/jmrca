package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService462 {
    public String performTask462() {
        return "Task 462 result";
    }
    
    public void crossCall(StressService463 other) {
        other.performTask463();
    }
}
