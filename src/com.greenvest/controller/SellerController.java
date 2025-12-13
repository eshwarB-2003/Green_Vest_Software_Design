

package com.greenvest.controller;

import com.greenvest.patterns.interceptor.*;
import com.greenvest.model.*;
import com.greenvest.service.SellerService;

import java.util.List;

public class SellerController {

    private InterceptorManager interceptorManager;
    private SellerService sellerService;

    public SellerController(SellerService service) {
        this.sellerService = service;

        interceptorManager = new InterceptorManager();
        interceptorManager.addInterceptor(new AuthenticationInterceptor());
        interceptorManager.addInterceptor(new RoleInterceptor("SELLER"));
    }

    public void submitAction(User seller, SustainabilityAction action) {
        if (!interceptorManager.execute(seller.getEmail(), seller.getRole()))
            return;

        sellerService.submitAction(action);
    }
    public List<Credit> getMyCredits(User seller) {

        if (!interceptorManager.execute(seller.getEmail(), seller.getRole()))
            return null;

        return sellerService.viewMyCredits(seller.getEmail());
    }
    public Credit generateCredits(User seller, SustainabilityAction action) {

        if (!interceptorManager.execute(seller.getEmail(), seller.getRole()))
            return null;

        return sellerService.generateCredits(action);

    }
    public boolean listCredit(User seller, Credit credit) {

        if (!interceptorManager.execute(seller.getEmail(), seller.getRole()))
            return false;

        return sellerService.listCredits(seller, credit);
    }

}

