package com.greenvest.repo;

import com.greenvest.model.Credit;
import java.util.List;

/*
 * CreditRepository defines methods
 * for storing and managing carbon credits.
 */
public interface CreditRepository {

    // Saves a new credit
    void save(Credit credit);

    // Returns all available (non-expired) credits
    List<Credit> getAvailableCredits();

    // Returns all credits for a specific seller
    List<Credit> getBySeller(String sellerEmail);

    // Updates an existing credit
    void updateCredit(Credit c);
}
