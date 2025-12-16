/* package com.greenvest.service;

import com.greenvest.config.SystemConfig;
import com.greenvest.model.*;
import com.greenvest.repo.*;
import com.greenvest.patterns.factory.*;
import com.greenvest.service.ActivityService;
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

*/

package com.greenvest.service;

import com.greenvest.config.SystemConfig;
import com.greenvest.model.*;
import com.greenvest.repo.*;
import com.greenvest.patterns.factory.*;
import com.greenvest.view.*;

import java.util.List;

public class AdminService {

    private ActionRepository actionRepo;
    private CreditRepository creditRepo;
    private ActivityService activityService = new ActivityService();

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

            Credit credit = CreditFactory.createCredit(
                    action.getSellerEmail(),
                    action.getType(),
                    action.getMetricValue()
            );

            creditRepo.save(credit);

            activityService.log(
                    "Approved sustainability action for seller: " + action.getSellerEmail(),
                    "ADMIN"
            );
        }
    public void rejectAction(SustainabilityAction action) {

        action.reject();
        actionRepo.update(action);

        activityService.log(
                "Rejected sustainability action for seller: " + action.getSellerEmail(),
                "ADMIN"
        );
    }

    public List<SustainabilityAction> getApprovedActions() {
        return actionRepo.getApprovedActions();
    }

    public List<SustainabilityAction> getRejectedActions() {
        return actionRepo.getRejectedActions();
    }

    public void setMinimumCreditPrice(double price) {
        SystemConfig.setMinCreditPrice(price);

        activityService.log(
                "Minimum credit price updated to " + price,
                "ADMIN"
        );
    }
}
