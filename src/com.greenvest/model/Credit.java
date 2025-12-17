// to store credit details

package com.greenvest.model;
import com.greenvest.patterns.state.*;
import java.time.LocalDate;

public class Credit {
    // credit id, seller mail, quantity, price, expiry
    private String id;
    private String sellerEmail;
    private int quantity;
    private double price;
    private LocalDate expiry;

  // credit state
    private CreditState state;
    // credit listed set to false
    private boolean listed = false;

    // localdate
    private LocalDate createdDate;

    // constructor to initialize credit details
    public Credit(String id,String sellerEmail, int quantity, double price, LocalDate expiry) {
        this.id = id;
        this.sellerEmail = sellerEmail;
        this.quantity = quantity;
        this.price = price;
        this.expiry = expiry;
        this.createdDate = LocalDate.now();
    }

    // get methods
    public String getId() { return id; }
    public String getSellerEmail() { return sellerEmail; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public LocalDate getExpiry() { return expiry; }
    // reduce quantity after sale
    public void reduceQuantity(int qty) {
       this.quantity -= qty;
    }

    // checks expiry
    public boolean isExpired() {
        return LocalDate.now().isAfter(expiry);
    }

    // current state
    public CreditState  getState() {
      return state;
    }

    // update state as per expiry
   public void updateState() {
        if (isExpired()) {
            state = new ExpiredState();
        }
        else if(isNearExpiry()){
            state = new NearExpiryState();
        }
        else {
            state = new ActiveState();
        }
    }

    // checl if near expiry
    public boolean isNearExpiry() {

        long totalDays = java.time.temporal.ChronoUnit.DAYS.between(createdDate, expiry);

        long elapsedDays = java.time.temporal.ChronoUnit.DAYS.between( createdDate, java.time.LocalDate.now());

        return elapsedDays >= (0.7 * totalDays) && !isExpired();
    }

    // return whether listed
    public boolean isListed() {
        return listed;
    }

    // marks credit as listed
    public void list() {
        this.listed = true;
    }
}