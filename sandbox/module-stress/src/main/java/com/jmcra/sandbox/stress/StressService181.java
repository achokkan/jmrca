package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService181 {
    public String performTask181() {
        return "Task 181 result";
    }
    
    public void crossCall(StressService182 other) {
        other.performTask182();
    }
}
