package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService585 {
    public String performTask585() {
        return "Task 585 result";
    }
    
    public void crossCall(StressService586 other) {
        other.performTask586();
    }
}
