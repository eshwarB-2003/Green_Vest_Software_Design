package com.greenvest.patterns.decorator;
import com.greenvest.model.Receipt;

public class HeaderDecorator extends ReceiptorDecorator {

    public HeaderDecorator(ReceiptPrinter printer) {
        super(printer);
    }

    @Override
    public void print(Receipt receipt) {
        System.out.println("===== GREENVEST RECEIPT =====");
        printer.print(receipt);
    }
}
