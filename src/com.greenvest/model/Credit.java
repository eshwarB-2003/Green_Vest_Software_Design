package com.greenvest.model;
import com.greenvest.patterns.state.*;
import java.time.LocalDate;

public class Credit {
    private String id;
    private String sellerEmail;
    private int quantity;
    private double price;
    private LocalDate expiry;
  //  private String state;
    private CreditState state;
    private boolean listed = false;
    private LocalDate createdDate;


    public Credit(String id,String sellerEmail, int quantity, double price, LocalDate expiry) {
        this.id = id;
        this.sellerEmail = sellerEmail;
        this.quantity = quantity;
        this.price = price;
        this.expiry = expiry;
        this.createdDate = LocalDate.now();
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

    public CreditState  getState() {
      return state;
    }

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
    public boolean isNearExpiry() {

        long totalDays =
                java.time.temporal.ChronoUnit.DAYS.between(
                        createdDate, expiry
                );

        long elapsedDays =
                java.time.temporal.ChronoUnit.DAYS.between(
                        createdDate, java.time.LocalDate.now()
                );

        return elapsedDays >= (0.7 * totalDays) && !isExpired();
    }

    public boolean isListed() {
        return listed;
    }

    public void list() {
        this.listed = true;
    }
}