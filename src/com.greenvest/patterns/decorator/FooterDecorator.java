package com.greenvest.patterns.decorator;


import com.greenvest.model.Receipt;

public class FooterDecorator extends ReceiptorDecorator {

    public FooterDecorator(ReceiptPrinter printer) {
        super(printer);
    }

    @Override
    public void print(Receipt receipt) {
        printer.print(receipt);
        System.out.println("Thank you for supporting sustainability.");
    }
}
