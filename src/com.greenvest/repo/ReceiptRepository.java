package com.greenvest.repo;


import com.greenvest.model.Receipt;
import java.util.List;

public interface ReceiptRepository {
    void saveReceipt(Receipt r);
    List<Receipt> getReceiptsByBuyer(String buyerEmail);
    List<Receipt> getAllReceipts();
}
