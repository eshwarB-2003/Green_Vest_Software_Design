package com.greenvest.rules;

import com.greenvest.model.Credit;
import com.greenvest.model.User;

import java.util.ArrayList;
import java.util.List;

public class RuleEngineService {

    private List<RulePlugin> rules = new ArrayList<>();

    public void registerRule(RulePlugin rule) {
        rules.add(rule);
    }

    public boolean validatePurchase(User buyer, Credit credit, int qty) {
        for (RulePlugin rule : rules) {
            if (!rule.apply(buyer, credit, qty))
                return false;
        }
        return true;
    }
    public boolean validateListing(User user,Credit credit) {
        for (RulePlugin rule : rules) {
            if (!rule.apply(user, credit, credit.getQuantity())) {
                return false;
            }
        }
        return true;
    }


}
