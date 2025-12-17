// checcks if purchase quantity is valid
// to prevent negative or excess credit purchse

package com.greenvest.rules;

import com.greenvest.model.Credit;
import com.greenvest.model.User;

public class ComplianceRule implements RulePlugin {

    // quantity must be greater than 0 and must not exceed available quantity
    @Override
    public boolean apply(User buyer, Credit credit, int qty) {
        return qty > 0 && qty <= credit.getQuantity();
    }
}
