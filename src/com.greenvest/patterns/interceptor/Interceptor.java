package com.greenvest.patterns.interceptor;

public interface Interceptor {
    boolean preHandle(String email, String role);
}
