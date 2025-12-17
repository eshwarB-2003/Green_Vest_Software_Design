package com.greenvest.model;
/* No setters are provided to keep the object immutable after creation
    All required fields are initialized via constructor
    Presentation / printing logic is handled separately using Decorator Pattern */
public class Receipt {
    // Unique identifier for the transaction (UUID generated)
    private String id;
    // Identifier of the credit batch purchased
    private String creditId;
    // Quantity of credits purchased in this transaction
    private int quantity;
    //total cost of the transaction (price × quantity)
    private double totalCost;
    // Email of the buyer who performed the purchas
    private String buyerEmail;

    /* Constructor initializes all receipt fields
       Ensures receipt is fully populated at creation time */

    public Receipt(String id, String creditId, int quantity, double totalCost, String buyerEmail) {
        this.id = id;
        this.creditId = creditId;
        this.quantity = quantity;
        this.totalCost = totalCost;
        this.buyerEmail = buyerEmail;
    }
    // return unique receipt id
    public String getId() {
        return id;
    }
    // return creditId associated with this receipt
    public String getCreditId() {
        return creditId;
    }
    // return quantity of credits purchased
    public int getQuantity() {
        return quantity;
    }
    //return total transaction cost
    public double getTotalCost() {
        return totalCost;
    }
    //   returns total transaction cost
    public String getBuyerEmail() {
        return buyerEmail;
    }
}
