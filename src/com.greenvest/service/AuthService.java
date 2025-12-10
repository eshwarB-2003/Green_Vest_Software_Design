package com.greenvest.service;

import com.greenvest.model.User;
import com.greenvest.repo.UsersRepository;
import java.security.MessageDigest;
import java.util.List;

public class AuthService {

    private UsersRepository repo = UsersRepository.getInstance();

    public boolean register(String name, String email, String password, String role) {
        List<User> users = repo.loadUsers();

        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) return false;
        }

        String hashed = hash(password);
        users.add(new User(name, email, role, hashed, 0.0));
        repo.saveUsers(users);
        return true;
    }

    public User login(String email, String password) {
        String hashed = hash(password);
        for (User u : repo.loadUsers()) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getPasswordHash().equals(hashed)) {
                return u;
            }
        }
        return null;
    }

    private String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] arr = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : arr) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }
}
