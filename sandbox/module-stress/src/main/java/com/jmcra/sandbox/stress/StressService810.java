package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService810 {
    public String performTask810() {
        return "Task 810 result";
    }
    
    public void crossCall(StressService811 other) {
        other.performTask811();
    }
}
