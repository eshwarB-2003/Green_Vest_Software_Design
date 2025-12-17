package com.greenvest.patterns.decorator;

import com.greenvest.model.Receipt;

/*
 * FooterDecorator adds a footer message
 * after the receipt details are printed.
 */
public class FooterDecorator extends ReceiptorDecorator {

    // Constructor wraps another ReceiptPrinter
    public FooterDecorator(ReceiptPrinter printer) {
        super(printer);
    }

    // Prints receipt and then adds footer text
    @Override
    public void print(Receipt receipt) {

        // Call wrapped printer first
        printer.print(receipt);

        // Add footer message
        System.out.println("Thank you for supporting sustainability.");
    }
}
