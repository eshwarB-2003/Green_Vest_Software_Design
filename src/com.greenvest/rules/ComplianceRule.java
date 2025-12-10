package com.greenvest.rules;

import com.greenvest.model.Credit;
import com.greenvest.model.User;

public class ComplianceRule implements RulePlugin {
    @Override
    public boolean apply(User buyer, Credit credit, int qty) {
        return qty > 0 && qty <= credit.getQuantity();
    }
}
