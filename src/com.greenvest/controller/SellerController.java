package com.greenvest.controller;


import com.greenvest.patterns.interceptor.*;
import com.greenvest.model.User;

public class SellerController {

    private InterceptorManager InterceptorManager;

    public SellerController() {
        InterceptorManager = new InterceptorManager();
        InterceptorManager.addInterceptor(new AuthenticationInterceptor());
        InterceptorManager.addInterceptor(new RoleInterceptor("SELLER"));
    }

    public void openDashboard(User user) {
        if (!InterceptorManager.execute(user.getEmail(), user.getRole())) return;

        System.out.println("===== SELLER DASHBOARD =====");
        System.out.println("Welcome " + user.getName());
    }
}
