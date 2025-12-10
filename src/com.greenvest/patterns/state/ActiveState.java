package com.greenvest.patterns.state;

public class ActiveState implements CreditState {
    public String getStateName() {
        return "ACTIVE";
    }
}
