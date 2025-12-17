package com.greenvest.patterns.decorator;

import com.greenvest.model.Receipt;

/*
 * ReceiptPrinter defines a common method
 * for printing receipt details.
 */
public interface ReceiptPrinter {

    // Prints receipt information
    void print(Receipt receipt);
}
