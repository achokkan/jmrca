package com.jmcra.sandbox.stress;

import org.springframework.stereotype.Service;

@Service
public class StressService586 {
    public String performTask586() {
        return "Task 586 result";
    }
    
    public void crossCall(StressService587 other) {
        other.performTask587();
    }
}
