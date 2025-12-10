package com.greenvest.repo;
import com.greenvest.model.Credit;


import java.util.List;

public interface  PortfolioRepository {
    List<Credit> getCreditsByBuyer(String email);
    void saveCreditForBuyer(String email, Credit credit);
}
