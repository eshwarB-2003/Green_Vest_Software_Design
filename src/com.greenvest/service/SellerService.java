package com.greenvest.service;

import com.greenvest.patterns.factory.*;
import com.greenvest.model.*;
import com.greenvest.repo.*;
import com.greenvest.rules.RuleEngineService;

import java.util.List;

public class SellerService {

    private ActionRepository actionRepo;
    private CreditRepository creditRepo;
    private RuleEngineService ruleEngine;

    public SellerService(ActionRepository aRepo,
                         CreditRepository cRepo,
                         RuleEngineService engine) {
        this.actionRepo = aRepo;
        this.creditRepo = cRepo;
        this.ruleEngine = engine;
    }

    public void submitAction(SustainabilityAction action) {
        actionRepo.save(action);
    }

    public Credit generateCredits(SustainabilityAction action) {
        if (action.isPending()){
            System.out.println("Action not approved yet.");
        return null;
    }
        Credit credit = CreditFactory.createCredit(
                action.getSellerEmail(),
                action.getType(),
                action.getMetricValue()
        );

        creditRepo.save(credit);
        return credit;
    }

    public boolean listCredits(User seller, Credit credit) {
        if (!ruleEngine.validateListing(seller, credit))
            return false;
        credit.list();
        System.out.println("DEBUG BEFORE UPDATE: listed = " + credit.isListed());
        creditRepo.updateCredit(credit);
        System.out.println("DEBUG AFTER UPDATE");
        // If valid  credit is considered listed
        return true;
    }



    public List<Credit> viewMyCredits(String sellerEmail) {
        return creditRepo.getBySeller(sellerEmail);
    }
}

