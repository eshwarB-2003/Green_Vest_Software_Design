package com.greenvest.model;
import com.greenvest.patterns.state.*;
import java.time.LocalDate;

public class Credit {
    private String id;
    private String sellerEmail;
    private int quantity;
    private double price;
    private LocalDate expiry;
    private String state;
    private boolean listed = false;


    public Credit(String id,String sellerEmail, int quantity, double price, LocalDate expiry) {
        this.id = id;
        this.sellerEmail = sellerEmail;
        this.quantity = quantity;
        this.price = price;
        this.expiry = expiry;
        this.state = "ACTIVE";
    }

    public String getId() { return id; }
    public String getSellerEmail() { return sellerEmail; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public LocalDate getExpiry() { return expiry; }
    public void reduceQuantity(int qty) {
       this.quantity -= qty;
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expiry);
    }

    public String getState() {
        if (isExpired()) state = "EXPIRED";
        return state;
    }


    /*public void updateState() {
        if (LocalDate.now().isAfter(expiry)) {
            this.state = new ExpiredState();
        } else {
            this.state = new ActiveState();
        }
    }
*/
   public void updateState() {
        if (isExpired()) {
            this.state = "EXPIRED";
        } else {
            this.state = "ACTIVE";
        }
    }

    public boolean isListed() {
        return listed;
    }

    public void list() {
        this.listed = true;
    }
}