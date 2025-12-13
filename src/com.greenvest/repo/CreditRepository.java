package com.greenvest.repo;

import com.greenvest.model.Credit;
import java.util.List;

public interface CreditRepository {
    void save(Credit credit);
    List<Credit> getAvailableCredits();
    List<Credit> getBySeller(String sellerEmail);
    void updateCredit(Credit c);
}