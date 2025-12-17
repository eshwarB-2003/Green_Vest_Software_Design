package com.greenvest.service;
import com.greenvest.model.Credit;
import com.greenvest.model.Receipt;
import com.greenvest.model.User;
import com.greenvest.patterns.state.ActiveState;
import com.greenvest.patterns.state.ExpiredState;
import com.greenvest.patterns.state.NearExpiryState;
import com.greenvest.repo.*;
import com.greenvest.repo.ReceiptRepository;
import com.greenvest.rules.RuleEngineService;
import com.greenvest.patterns.observer.*;
import java.util.*;

import java.util.List;
// Service-layer class containing all Buyer-related business logic.
public class BuyerService {
    // Repository dependencies (Repository Pattern)
    private CreditRepository creditRepo;
    private ReceiptRepository receiptRepo;
    private PortfolioRepository portfolioRepo ;
    // Observer subject for expiry alerts
    private AlertService alertService;
    // Business rule engine
    private RuleEngineService ruleEngine;
    // Activity logger (audit / traceability)
    ActivityService activityService = new ActivityService();
    // Constructor injects repositories and rule engine
    public BuyerService(CreditRepository creditRep
                        , ReceiptRepository receiptRepo
                        , PortfolioRepository portfolioRepo,
                        RuleEngineService ruleEngine
                         ) {
        this.creditRepo = creditRep;
        this.receiptRepo = receiptRepo;
        this.portfolioRepo = portfolioRepo;
        this.alertService = new AlertService();
        this.ruleEngine = ruleEngine;
        this.alertService = new AlertService();
    }
// ----------------MarketPlace -----------------
 /*Loads marketplace credits available for purchase
 Applies basic filtering rules*/
    public List<Credit> loadAvailableCredits() {
        return creditRepo.getAvailableCredits().stream()
                .filter(Credit::isListed)
                .filter(c -> c.getQuantity() >0)
                .toList();
    }
    /* Core purchase use case
       Executes validation, balance updates, portfolio updates,
       receipt generation, and persistence */

    public Receipt processPurchase(User buyer, Credit credit, int qty) {
        activityService.log(
                "Buyer purchased credits | Qty: " + qty + " | Credit ID: " + credit.getId(),
                buyer.getEmail()
        );
        // Validate business rules via Rule Engine
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
        Receipt receipt = new Receipt(
                UUID.randomUUID().toString(),
                credit.getId(),
                qty,
                cost,
                buyer.getEmail()
        );
        receiptRepo.saveReceipt(receipt);

        //  RETURN receipt is last returns to controller
        return receipt;
    }

    // portfolio
    /* Returns buyer portfolio
       Applies State Pattern and triggers alerts via Observer Pattern */
    public List<Credit> getPortfolio(User buyer) {
        alertService.attach(
                new BuyerAlertObserver(buyer.getEmail())
        );
        List<Credit> credits = portfolioRepo.getCreditsByBuyer(buyer.getEmail());

        for (Credit c : credits) {
            // Attach observer dynamically for this buyer
            c.updateState();  // STATE PATTERN
            // Notify buyer based on state
            if (c.getState() instanceof ExpiredState) {
                alertService.notifyObservers("Your credit " + c.getId() + " has expired!");
            }
            if(c.getState() instanceof NearExpiryState) {
                alertService.notifyObservers(
                        "Your credit " + c.getId() + " is nearing expiry!"
                );
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

            if (c.getState() instanceof ActiveState)
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