package com.greenvest.controller;

import com.greenvest.patterns.interceptor.*;
import com.greenvest.model.*;
import com.greenvest.service.AdminService;

import java.util.List;

public class AdminController {

    private InterceptorManager interceptorManager;
    private AdminService adminService;

    public AdminController(AdminService service) {
        this.adminService = service;

        interceptorManager = new InterceptorManager();
        interceptorManager.addInterceptor(new AuthenticationInterceptor());
        interceptorManager.addInterceptor(new RoleInterceptor("ADMIN"));
    }

    public List<SustainabilityAction> viewPending(User admin) {
        if (!interceptorManager.execute(admin.getEmail(), admin.getRole()))
            return null;

        return adminService.getPendingActions();
    }

    public void approve(User admin, SustainabilityAction action) {
        if (!interceptorManager.execute(admin.getEmail(), admin.getRole()))
            return;
        adminService.approveAction(action);
    }
    public void reject(User admin, SustainabilityAction action) {
        if(!interceptorManager.execute(admin.getEmail(), admin.getRole()))
            return;
        adminService.rejectAction(action);
    }
    public List<SustainabilityAction> viewApproved(User admin) {
        if (!interceptorManager.execute(admin.getEmail(), admin.getRole()))
            return null;

        return adminService.getApprovedActions();
    }

    public List<SustainabilityAction> viewRejected(User admin) {
        if (!interceptorManager.execute(admin.getEmail(), admin.getRole()))
            return null;

        return adminService.getRejectedActions();
    }
    public void setMinPrice(User admin, double price) {

        if (!interceptorManager.execute(admin.getEmail(), admin.getRole()))
            return;

        adminService.setMinimumCreditPrice(price);
    }
}