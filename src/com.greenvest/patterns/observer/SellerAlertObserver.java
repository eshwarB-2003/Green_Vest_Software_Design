package com.greenvest.patterns.observer;

public class SellerAlertObserver implements Observer {

    private String sellerEmail;
    public SellerAlertObserver(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }
    public void update(String message) {
        System.out.println("⚠ ALERT for " + sellerEmail + ": " + message);
    }
}
