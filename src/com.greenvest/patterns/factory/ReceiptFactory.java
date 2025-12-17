package com.greenvest.patterns.factory;
import com.greenvest.model.Receipt;
import java.util.UUID;

/* Generate unique receipt identifiers (UUID)
ensures consistent and valid Receipt creation
 Decouple object creation from business logic*/

public class ReceiptFactory {

    public static Receipt createReceipt(String creditId, int qty, double cost, String email) {
        //  Generate unique receipt ID internally
        String receiptId = UUID.randomUUID().toString();
        // Create and return immutable Receipt entity
        return new Receipt(receiptId, creditId, qty, cost, email);
    }
}