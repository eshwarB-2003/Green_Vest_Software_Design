package com.greenvest.patterns.interceptor;
public class AuthenticationInterceptor implements Interceptor {

    @Override
    public boolean preHandle(String email, String role) {
        if (email == null || email.isEmpty()) {
            System.out.println(" Access denied. User not authenticated.");
            return false;
        }
        System.out.println(" Authenticated: " + email + " | Role: " + role);
        return true;
    }
}

