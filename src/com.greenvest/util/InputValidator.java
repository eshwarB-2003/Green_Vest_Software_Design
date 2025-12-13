package com.greenvest.util;
import java.util.regex.Pattern;

public class InputValidator {

    // Simple email pattern (OWASP-friendly for console apps)
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    // At least one letter, one number, min 6 chars
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).{6,}$");

    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        email = email.trim();
        return !email.isEmpty() && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        if (password == null) return false;
        password = password.trim();
        return !password.isEmpty() && PASSWORD_PATTERN.matcher(password).matches();
    }
}
