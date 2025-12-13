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

    private CreditRepository creditRepo;
    private ReceiptRepository receiptRepo;
    private PortfolioRepository portfolioRepo ;
    private AlertService alertService;
    private RuleEngineService ruleEngine;
    ActivityService activityService = new ActivityService();

    public BuyerService(CreditRepository creditRep
                        , ReceiptRepository receiptRepo
                        , PortfolioRepository portfolioRepo,
                        RuleEngineService ruleEngine) {
        this.creditRepo = creditRep;
        this.receiptRepo = receiptRepo;
        this.portfolioRepo = portfolioRepo;
        this.alertService = new AlertService();
        this.ruleEngine = ruleEngine;
    }
// ----------------MarketPlace -----------------
    public List<Credit> loadAvailableCredits() {
        return creditRepo.getAvailableCredits().stream()
                .filter(Credit::isListed)
                .filter(c -> c.getQuantity() >0)
                .toList();
    }

    public Receipt processPurchase(User buyer, Credit credit, int qty) {
        activityService.log(
                "Buyer purchased credits | Qty: " + qty + " | Credit ID: " + credit.getId(),
                buyer.getEmail()
        );

        if (!ruleEngine.validatePurchase(buyer, credit, qty)) {
            return null;
        }

        double cost = credit.getPrice() * qty;

        if (buyer.getBalance() < cost) {
            return null;
        }

        // Deduct balance
        buyer.deductBalance(cost);

        // Reduce seller credit quantity
        credit.reduceQuantity(qty);
        creditRepo.updateCredit(credit);

        // Add credit to buyer portfolio
        Credit purchased = new Credit(
                UUID.randomUUID().toString(),
                credit.getSellerEmail(),
                qty,
                credit.getPrice(),
                credit.getExpiry()
        );
        portfolioRepo.saveCreditForBuyer(buyer.getEmail(), purchased);

        //  CREATE RECEIPT BEFORE RETURN
       // double cost =credit.getPrice() *qty;
        Receipt receipt = new Receipt(
                UUID.randomUUID().toString(),
                credit.getId(),
                qty,
                cost,
                buyer.getEmail()
        );
        receiptRepo.saveReceipt(receipt);

        //  RETURN IS LAST
        return receipt;
    }

    // portfolio
    public List<Credit> getPortfolio(User buyer) {
        List<Credit> credits = portfolioRepo.getCreditsByBuyer(buyer.getEmail());

        for (Credit c : credits) {
            c.updateState();  // STATE PATTERN

            if (c.getState().equals("EXPIRED")) {
                alertService.notifyObservers("Your credit " + c.getId() + " has expired!");
            }
        }

        return credits;
    }
    // receipts
    public List<Receipt> getReceipts(User buyer) {
        return receiptRepo.getReceiptsByBuyer(buyer.getEmail());
    }

    // account summary
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

            if (c.getState().equals("ACTIVE"))
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