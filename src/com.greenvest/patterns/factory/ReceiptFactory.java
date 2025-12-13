package com.greenvest.patterns.factory;
import com.greenvest.model.Receipt;
import java.util.UUID;

public class ReceiptFactory {

    public static Receipt createReceipt(String creditId, int qty, double cost, String email) {
        return new Receipt(UUID.randomUUID().toString(), creditId, qty, cost, email);
    }
}