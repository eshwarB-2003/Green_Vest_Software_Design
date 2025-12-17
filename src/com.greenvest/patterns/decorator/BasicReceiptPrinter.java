package com.greenvest.patterns.decorator;

import com.greenvest.model.Receipt;

/*
 * BasicReceiptPrinter prints the core
 * receipt details without any extras.
 */
public class BasicReceiptPrinter implements ReceiptPrinter {

    // Prints basic receipt information
    @Override
    public void print(Receipt r) {

        System.out.println("\nReceipt ID: " + r.getId());
        System.out.println("Credit ID: " + r.getCreditId());
        System.out.println("Quantity: " + r.getQuantity());
        System.out.println("Total Cost: " + r.getTotalCost());
    }
}
