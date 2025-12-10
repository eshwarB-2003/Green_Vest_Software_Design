package com.greenvest.patterns.observer;



public class BuyerAlertObserver implements Observer {

    private String buyerEmail;

    public BuyerAlertObserver(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    @Override
    public void update(String message) {
        System.out.println("⚠ ALERT for " + buyerEmail + ": " + message);
    }
}
