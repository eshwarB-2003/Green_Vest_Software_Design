// interface for all business rules

package com.greenvest.rules;

import com.greenvest.model.Credit;
import com.greenvest.model.User;

public abstract interface RulePlugin { 
    // apply rule for user credit and quantity
    boolean apply(User buyer, Credit credit, int qty);
}
