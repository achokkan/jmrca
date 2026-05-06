package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService995 {
    public String performTask995() {
        return "Task 995 result";
    }
    
    public void crossCall(StressService996 other) {
        other.performTask996();
    }
}
