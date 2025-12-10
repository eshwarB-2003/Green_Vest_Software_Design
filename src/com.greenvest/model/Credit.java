package com.greenvest.model;
import com.greenvest.patterns.state.*;
import java.time.LocalDate;

public class Credit {
    private String id;
    private int quantity;
    private double price;
    private LocalDate expiry;

    public Credit(String id, int quantity, double price, LocalDate expiry) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.expiry = expiry;
    }

    public String getId() { return id; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public LocalDate getExpiry() { return expiry; }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expiry);
    }
    private CreditState state;

    public CreditState getState() {
        return state;
    }

    public void updateState() {
        if (LocalDate.now().isAfter(expiry)) {
            this.state = new ExpiredState();
        } else {
            this.state = new ActiveState();
        }
    }
}
