package com.greenvest.patterns.interceptor;
import java.util.ArrayList;
import java.util.List;

public class InterceptorManager {
    private List<Interceptor> list = new ArrayList<>();

    public void addInterceptor(Interceptor i) { list.add(i); }

    public boolean execute(String email, String role) {
        for (Interceptor i : list) {
            if (!i.preHandle(email, role)) return false;
        }
        return true;
    }
}

