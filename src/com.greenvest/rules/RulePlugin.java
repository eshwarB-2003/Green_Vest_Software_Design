package com.greenvest.rules;

import com.greenvest.model.Credit;
import com.greenvest.model.User;

public interface RulePlugin {
    boolean apply(User buyer, Credit credit, int qty);
}
