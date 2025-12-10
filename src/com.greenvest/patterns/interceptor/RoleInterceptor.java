package com.greenvest.patterns.interceptor;

public class RoleInterceptor implements Interceptor {

    private String requiredRole;

    public RoleInterceptor(String requiredRole) {
        this.requiredRole = requiredRole;
    }

    @Override
    public boolean preHandle(String email, String role) {
        if (!role.equals(requiredRole)) {
            System.out.println(" Role restricted. Only " + requiredRole + " allowed.");
            return false;
        }
        return true;
    }
}

