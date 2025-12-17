package com.greenvest.patterns.decorator;

import com.greenvest.model.Receipt;

/*
 * ReceiptorDecorator is the base class
 * for all receipt decorators.
 * It wraps another ReceiptPrinter.
 */
public abstract class ReceiptorDecorator implements ReceiptPrinter {

    // Wrapped receipt printer
    protected ReceiptPrinter printer;

    // Constructor sets the wrapped printer
    public ReceiptorDecorator(ReceiptPrinter printer) {
        this.printer = printer;
    }
}
