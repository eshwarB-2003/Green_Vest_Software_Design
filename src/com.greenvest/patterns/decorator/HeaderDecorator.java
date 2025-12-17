package com.greenvest.patterns.decorator;

import com.greenvest.model.Receipt;

/*
 * HeaderDecorator adds a header
 * before printing the receipt details.
 */
public class HeaderDecorator extends ReceiptorDecorator {

    // Constructor wraps another ReceiptPrinter
    public HeaderDecorator(ReceiptPrinter printer) {
        super(printer);
    }

    // Prints header and then calls wrapped printer
    @Override
    public void print(Receipt receipt) {

        // Print receipt header
        System.out.println("===== GREENVEST RECEIPT =====");

        // Print receipt content
        printer.print(receipt);
    }
}
