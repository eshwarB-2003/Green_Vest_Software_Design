package com.greenvest.service;

import com.greenvest.patterns.factory.*;
import com.greenvest.model.*;
import com.greenvest.repo.*;
import com.greenvest.rules.RuleEngineService;

import java.util.List;

/*
 * SellerService contains business logic
 * related to seller actions and credits.
 */
public class SellerService {

    // Repository for sustainability actions
    private ActionRepository actionRepo;

    // Repository for seller credits
    private CreditRepository creditRepo;

    // Rule engine for validation checks
    private RuleEngineService ruleEngine;

    // Constructor initialises repositories and rule engine
    public SellerService(ActionRepository aRepo,
                         CreditRepository cRepo,
                         RuleEngineService engine) {
        this.actionRepo = aRepo;
        this.creditRepo = cRepo;
        this.ruleEngine = engine;
    }

    // Saves a sustainability action submitted by seller
    public void submitAction(SustainabilityAction action) {
        actionRepo.save(action);
    }

    /*
     * Generates credits for an approved
     * sustainability action.
     */
    public Credit generateCredits(SustainabilityAction action) {

        // Prevent credit generation if not approved
        if (action.isPending()) {
            System.out.println("Action not approved yet.");
            return null;
        }

        // Create credits using factory
        Credit credit = CreditFactory.createCredit(
                action.getSellerEmail(),
                action.getType(),
                action.getMetricValue()
        );

        // Save generated credits
        creditRepo.save(credit);
        return credit;
    }

    /*
     * Lists seller credits in the marketplace
     * after validating listing rules.
     */
    public boolean listCredits(User seller, Credit credit) {

        // Validate listing rules
        if (!ruleEngine.validateListing(seller, credit))
            return false;

        // Mark credit as listed
        credit.list();

        // Update credit status in repository
        creditRepo.updateCredit(credit);

        return true;
    }

    // Returns all credits owned by the seller
    public List<Credit> viewMyCredits(String sellerEmail) {
        return creditRepo.getBySeller(sellerEmail);
    }
}
