package com.greenvest.model;
public class Receipt {
    private String id;
    private String creditId;
    private int quantity;
    private double totalCost;
    private String buyerEmail;

    public Receipt(String id, String creditId, int quantity, double totalCost, String buyerEmail) {
        this.id = id;
        this.creditId = creditId;
        this.quantity = quantity;
        this.totalCost = totalCost;
        this.buyerEmail = buyerEmail;
    }

    public String getId() {
        return id;
    }
    public String getCreditId() {
        return creditId;
    }
    public int getQuantity() {
        return quantity;
    }
    public double getTotalCost() {
        return totalCost;
    }
    public String getBuyerEmail() {
        return buyerEmail;
    }
}
