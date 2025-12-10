package com.greenvest.repo;

import com.greenvest.model.User;

import java.io.*;
import java.util.*;

public class UsersRepository {

    private static UsersRepository instance;

    private final String FILE_PATH = "data/users.json";

    private UsersRepository() {}

    public static synchronized UsersRepository getInstance() {
        if (instance == null) instance = new UsersRepository();
        return instance;
    }

    // ----------------------
    // Load Users (Simple JSON Parser)
    // ----------------------
    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();

        try {
            File f = new File(FILE_PATH);
            if (!f.exists()) return users;

            BufferedReader br = new BufferedReader(new FileReader(f));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null)
                sb.append(line);

            br.close();

            String raw = sb.toString()
                    .replace("[", "")
                    .replace("]", "");

            if (raw.trim().isEmpty()) return users;

            String[] entries = raw.split("\\},\\s*\\{");

            for (String entry : entries) {

                entry = entry.replace("{", "").replace("}", "");

                Map<String, String> map = new HashMap<>();

                String[] pairs = entry.split(",");
                for (String pair : pairs) {
                    String[] kv = pair.split(":");
                    if (kv.length == 2) {
                        String key = kv[0].replace("\"", "").trim();
                        String val = kv[1].replace("\"", "").trim();
                        map.put(key, val);
                    }
                }

                User u = new User(
                        map.get("name"),
                        map.get("email"),
                        map.get("role"),
                        map.get("passwordHash"),
                        Double.parseDouble(map.getOrDefault("balance", "0"))
                );

                users.add(u);
            }

        } catch (Exception e) {
            System.out.println("Error loading users: " + e.getMessage());
        }

        return users;
    }

    // ----------------------
    // Save Users (Write JSON manually)
    // ----------------------



    public void saveUsers(List<User> users) {
        try {
            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs();   // <-- CREATE "data" folder automatically
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH));

            bw.write("[\n");

            for (int i = 0; i < users.size(); i++) {
                User u = users.get(i);

                bw.write("  {\n");
                bw.write("    \"name\": \"" + u.getName() + "\",\n");
                bw.write("    \"email\": \"" + u.getEmail() + "\",\n");
                bw.write("    \"role\": \"" + u.getRole() + "\",\n");
                bw.write("    \"passwordHash\": \"" + u.getPasswordHash() + "\",\n");
                bw.write("    \"balance\": " + u.getBalance() + "\n");
                bw.write("  }");

                if (i < users.size() - 1) bw.write(",");

                bw.write("\n");
            }

            bw.write("]");

            bw.close();

        } catch (Exception e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }
}

