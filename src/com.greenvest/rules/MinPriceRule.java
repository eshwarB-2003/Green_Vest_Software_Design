package com.greenvest.rules;


import com.greenvest.config.SystemConfig;
import com.greenvest.model.Credit;
import com.greenvest.model.User;

public class MinPriceRule implements RulePlugin {

    @Override
    public boolean apply(User seller, Credit credit,int qty) {
        return credit.getPrice() >= SystemConfig.getMinCreditPrice();
    }
}
