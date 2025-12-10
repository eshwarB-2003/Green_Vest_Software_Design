package com.greenvest.patterns.decorator;


import com.greenvest.model.Receipt;

public abstract class ReceiptorDecorator implements ReceiptPrinter {
    protected ReceiptPrinter printer;

    public ReceiptorDecorator(ReceiptPrinter printer) {
        this.printer = printer;
    }
}
