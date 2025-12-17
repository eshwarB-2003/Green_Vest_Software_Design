// manages and executes all rules

package com.greenvest.rules;

import com.greenvest.model.Credit;
import com.greenvest.model.User;

import java.util.ArrayList;
import java.util.List;

public class RuleEngineService {

    // stores all rules
    private List<RulePlugin> rules = new ArrayList<>();

    // register new rule in engine
    public void registerRule(RulePlugin rule) {
        rules.add(rule);
    }

    // validate purchase
    public boolean validatePurchase(User buyer, Credit credit, int qty) {
        for (RulePlugin rule : rules) {
            if (!rule.apply(buyer, credit, qty))
                return false;
        }
        return true;
    }

    // validate listing
    public boolean validateListing(User user,Credit credit) {
        for (RulePlugin rule : rules) {
            if (!rule.apply(user, credit, credit.getQuantity())) {
                return false;
            }
        }
        return true;
    }


}
