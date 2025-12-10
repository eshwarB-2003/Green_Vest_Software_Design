package com.greenvest.service;
import com.greenvest.model.Credit;
import com.greenvest.model.Receipt;
import com.greenvest.model.User;
import com.greenvest.repo.*;
import com.greenvest.repo.ReceiptRepository;
import com.greenvest.rules.RuleEngineService;
import com.greenvest.patterns.observer.*;
import java.util.*;

import java.util.List;

public class BuyerService {

    private CreditRepositoryJSON repo = CreditRepositoryJSON.getInstance();
    private ReceiptRepository receiptRepo = ReceiptRepositoryJSON.getInstance();
    private PortfolioRepository portfolioRepo = PortfolioRepositoryJSON.getInstance();
    private AlertService alertService = new AlertService();


    private RuleEngineService ruleEngine;

    public BuyerService(RuleEngineService ruleEngine) {
        this.ruleEngine = ruleEngine;
    }

    public List<Credit> loadAvailableCredits() {
        return repo.getAvailableCredits();
    }

    public boolean processPurchase(User buyer, Credit credit, int qty) {

        if (!ruleEngine.validatePurchase(buyer, credit, qty)) {
            return false;
        }

        double cost = credit.getPrice() * qty;

        if (buyer.getBalance() < cost)
            return false;

        buyer.deductBalance(cost);

        // Update repository to reduce credit qty
        Credit updated = new Credit(
                credit.getId(),
                credit.getQuantity() - qty,
                credit.getPrice(),
                credit.getExpiry()
        );

        repo.updateCredit(updated);

        return true;
    }
    public List<Credit> getPortfolio(User buyer) {
        List<Credit> credits = portfolioRepo.getCreditsByBuyer(buyer.getEmail());

        for (Credit c : credits) {
            c.updateState();  // STATE PATTERN

            if (c.getState().getStateName().equals("EXPIRED")) {
                alertService.notifyObservers("Your credit " + c.getId() + " has expired!");
            }
        }

        return credits;
    }
    public List<Receipt> getReceipts(User buyer) {
        return receiptRepo.getReceiptsByBuyer(buyer.getEmail());
    }
    public Map<String, Object> getAccountSummary(User buyer) {

        Map<String, Object> summary = new HashMap<>();

        // Buyer balance
        summary.put("balance", buyer.getBalance());

        // Load portfolio
        List<Credit> portfolio = portfolioRepo.getCreditsByBuyer(buyer.getEmail());

        int totalCredits = 0;
        int active = 0;
        int expired = 0;

        for (Credit c : portfolio) {
            c.updateState();  // Apply State Pattern

            totalCredits += c.getQuantity();

            if (c.getState().getStateName().equals("ACTIVE"))
                active += c.getQuantity();
            else
                expired += c.getQuantity();
        }

        summary.put("totalCredits", totalCredits);
        summary.put("activeCredits", active);
        summary.put("expiredCredits", expired);

        return summary;
    }



}
