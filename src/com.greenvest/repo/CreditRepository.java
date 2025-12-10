package com.greenvest.repo;

import com.greenvest.model.Credit;
import java.util.List;

public interface CreditRepository {
    List<Credit> getAvailableCredits();
    void updateCredit(Credit c);
}

