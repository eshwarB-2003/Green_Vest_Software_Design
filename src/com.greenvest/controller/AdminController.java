// control layer between admin view and admin service


package com.greenvest.controller;

import com.greenvest.patterns.interceptor.*;
import com.greenvest.model.*;
import com.greenvest.service.AdminService;

import java.util.List;

public class AdminController {

    // manage authentication and role based access
    private InterceptorManager interceptorManager;

    // service containing admin business logic
    private AdminService adminService;

    // contructor to initialze service and set interceptor
    public AdminController(AdminService service) {
        this.adminService = service;

        // create interceptor manager
        interceptorManager = new InterceptorManager();
        // authenticate admin
        interceptorManager.addInterceptor(new AuthenticationInterceptor());
        // ensure user has admin role
        interceptorManager.addInterceptor(new RoleInterceptor("ADMIN"));
    }

    // returns all pending sustainability action
    public List<SustainabilityAction> viewPending(User admin) {

        // interceptor check
        if (!interceptorManager.execute(admin.getEmail(), admin.getRole()))
            return null;

        // pending action from service
        return adminService.getPendingActions();
    }

    // approve action
    public void approve(User admin, SustainabilityAction action) {

        // verify admin permission
        if (!interceptorManager.execute(admin.getEmail(), admin.getRole()))
            return;
        // service to approve action
        adminService.approveAction(action);
    }

    // reject action
    public void reject(User admin, SustainabilityAction action) {
        // verify admin permission
        if(!interceptorManager.execute(admin.getEmail(), admin.getRole()))
            return;
        // service to reject
        adminService.rejectAction(action);
    }

    // return approved action
    public List<SustainabilityAction> viewApproved(User admin) {
        // verify admin role
        if (!interceptorManager.execute(admin.getEmail(), admin.getRole()))
            return null;

        // service to get action
        return adminService.getApprovedActions();
    }

    // return all rejected actions
    public List<SustainabilityAction> viewRejected(User admin) {
        // verify admin role
        if (!interceptorManager.execute(admin.getEmail(), admin.getRole()))
            return null;

        // service to get rejected action
        return adminService.getRejectedActions();
    }

    // set min price
    public void setMinPrice(User admin, double price) {

        // verify admin role
        if (!interceptorManager.execute(admin.getEmail(), admin.getRole()))
            return;

        // service to set min price
        adminService.setMinimumCreditPrice(price);
    }
}