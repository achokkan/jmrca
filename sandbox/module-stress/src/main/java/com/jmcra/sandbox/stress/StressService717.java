package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService717 {
    public String performTask717() {
        return "Task 717 result";
    }
    
    public void crossCall(StressService718 other) {
        other.performTask718();
    }
}
