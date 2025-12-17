

package com.greenvest.controller;

import com.greenvest.patterns.interceptor.*;
import com.greenvest.model.*;
import com.greenvest.service.SellerService;

import java.util.List;

/*
 * SellerController handles all Seller-related requests.
 * It validates access using interceptors and delegates
 * business logic to the SellerService.
 */

public class SellerController {

    // Manages authentication and role-based checks

    private InterceptorManager interceptorManager;

    // Service layer for seller-related operations
    private SellerService sellerService;

    // Constructor initialises service and interceptors
    public SellerController(SellerService service) {
        this.sellerService = service;

        interceptorManager = new InterceptorManager();
        interceptorManager.addInterceptor(new AuthenticationInterceptor());
        interceptorManager.addInterceptor(new RoleInterceptor("SELLER"));
    }

    // Allows a seller to submit a sustainability action
    public void submitAction(User seller, SustainabilityAction action) {

        // Check authentication and seller role before proceeding
        if (!interceptorManager.execute(seller.getEmail(), seller.getRole()))
            return;
        // Delegate submission logic to the service layer
        sellerService.submitAction(action);
    }
    // Returns all credits owned by the seller
    public List<Credit> getMyCredits(User seller) {

        // Access control check
        if (!interceptorManager.execute(seller.getEmail(), seller.getRole()))
            return null;

        // Fetch seller credits from service layer
        return sellerService.viewMyCredits(seller.getEmail());
    }
    // Generates credits for an approved sustainability action
    public Credit generateCredits(User seller, SustainabilityAction action) {

        // Ensure seller is authenticated and authorised
        if (!interceptorManager.execute(seller.getEmail(), seller.getRole()))
            return null;

        // Request credit generation from service
        return sellerService.generateCredits(action);

    }
    // Lists a credit for sale with a specified price
    public boolean listCredit(User seller, Credit credit) {

        // Perform security checks
        if (!interceptorManager.execute(seller.getEmail(), seller.getRole()))
            return false;

        // Delegate credit listing to service layer
        return sellerService.listCredits(seller, credit);
    }

}

