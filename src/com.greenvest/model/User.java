package com.greenvest.model;

public class User {

    private String name;
    private String email;
    private String role;   // ADMIN, SELLER, BUYER
    private String passwordHash;
    private double balance;

    public User(String name, String email, String role, String passwordHash, double balance) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.passwordHash = passwordHash;
        this.balance = 1000;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getPasswordHash() { return passwordHash; }
    public double getBalance() { return balance; }

    public void deductBalance(double amount) {
        this.balance -= amount;
    }
}
