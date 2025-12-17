// contains all business logic related to admin

package com.greenvest.service;

import com.greenvest.config.SystemConfig;
import com.greenvest.model.*;
import com.greenvest.repo.*;
import com.greenvest.patterns.factory.*;
import com.greenvest.view.*;

import java.util.List;

public class AdminService {

    // repo to manage action
    private ActionRepository actionRepo;

    // repo to manage credits
    private CreditRepository creditRepo;

    // service to log system activites
    private ActivityService activityService = new ActivityService();

    // constructor to inject repo
    public AdminService(ActionRepository aRepo, CreditRepository cRepo) {
        this.actionRepo = aRepo;
        this.creditRepo = cRepo;
    }

    // return all pending actions
    public List<SustainabilityAction> getPendingActions() {
        return actionRepo.getPendingActions();
    }

    // approve action
    public void approveAction(SustainabilityAction action) {

        // update action status to approve
          action.approve();
            actionRepo.update(action);

            // generate credit using factory
            Credit credit = CreditFactory.createCredit(
                    action.getSellerEmail(),
                    action.getType(),
                    action.getMetricValue()
            );

            // save to repo
            creditRepo.save(credit);

            // log activity
            activityService.log("Approved sustainability action for seller: " + action.getSellerEmail(),"ADMIN" );
    }

    // reject action
    public void rejectAction(SustainabilityAction action) {

        // update action status to reject
        action.reject();
        actionRepo.update(action);

        // log rejection activity
        activityService.log( "Rejected sustainability action for seller: " + action.getSellerEmail(), "ADMIN"  );
    }

    // return all approved action
    public List<SustainabilityAction> getApprovedActions() {
        return actionRepo.getApprovedActions();
    }

    // return all rejected action
    public List<SustainabilityAction> getRejectedActions() {
        return actionRepo.getRejectedActions();
    }

    // set minimum price
    public void setMinimumCreditPrice(double price) {
        // update price
        SystemConfig.setMinCreditPrice(price);

        // log activity
        activityService.log("Minimum credit price updated to " + price,"ADMIN" );
    }
}
