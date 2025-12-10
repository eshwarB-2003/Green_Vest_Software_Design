package com.greenvest.controller;

import com.greenvest.patterns.interceptor.*;
import com.greenvest.model.User;

public class AdminController {

    private InterceptorManager InterceptorManager;

    public AdminController() {
        InterceptorManager = new InterceptorManager();
        InterceptorManager.addInterceptor(new AuthenticationInterceptor());
        InterceptorManager.addInterceptor(new RoleInterceptor("ADMIN"));
    }

    public void openDashboard(User user) {
        if (!InterceptorManager.execute(user.getEmail(), user.getRole())) return;

        System.out.println("===== ADMIN DASHBOARD =====");
        System.out.println("Welcome Admin " + user.getName());
    }
}
