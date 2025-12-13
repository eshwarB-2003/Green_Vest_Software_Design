package com.greenvest.rules;

import com.greenvest.model.Credit;
import com.greenvest.model.User;

import java.time.LocalDate;

public class ListingRule implements RulePlugin {
    private static final double MIN_PRICE = 10.0;

    @Override
    public boolean apply (User user, Credit credit, int qty) {

        // Rule 1: Credit must not be expired
        if (credit.getExpiry().isBefore(LocalDate.now())) {
            System.out.println(" Cannot list expired credit.");
            return false;
        }

        // Rule 2: Minimum price rule
        if (credit.getPrice() < MIN_PRICE) {
            System.out.println(" Price below minimum allowed.");
            return false;
        }

        // Rule 3: Quantity must be positive
        if (credit.getQuantity() <= 0) {
            System.out.println(" Invalid credit quantity.");
            return false;
        }

        return true;
    }
}
