package com.greenvest.rules;

import com.greenvest.model.Credit;
import com.greenvest.model.User;

public abstract interface RulePlugin { ;
    boolean apply(User buyer, Credit credit, int qty);
}
