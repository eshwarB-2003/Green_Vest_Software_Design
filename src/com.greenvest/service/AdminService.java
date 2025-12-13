package com.greenvest.service;

import com.greenvest.config.SystemConfig;
import com.greenvest.model.*;
import com.greenvest.repo.*;
import com.greenvest.patterns.factory.*;

import java.util.List;

public class AdminService {

    private ActionRepository actionRepo;
    private CreditRepository creditRepo;

    public AdminService(ActionRepository aRepo, CreditRepository cRepo) {
        this.actionRepo = aRepo;
        this.creditRepo = cRepo;
    }

    public List<SustainabilityAction> getPendingActions() {
        return actionRepo.getPendingActions();
    }

    public void approveAction(SustainabilityAction action) {


        action.approve();
        actionRepo.update(action);

        //GENERATE CREDITS
        Credit credit = CreditFactory.createCredit(
                action.getSellerEmail(),
                action.getType(),
                action.getMetricValue()
        );

        creditRepo.save(credit);
    }
    public void rejectAction(SustainabilityAction action) {

        System.out.println("DEBUG: rejectAction() CALLED for ID = " + action.getId());

        action.reject();
        actionRepo.update(action);
    }
    public List<SustainabilityAction> getApprovedActions() {
        return actionRepo.getApprovedActions();
    }

    public List<SustainabilityAction> getRejectedActions() {
        return actionRepo.getRejectedActions();
    }
    public void setMinimumCreditPrice(double price) {
        SystemConfig.setMinCreditPrice(price);
    }




}

