package com.greenvest.repo;

import com.greenvest.model.Credit;

import java.io.*;
import java.util.*;

public class PortfolioRepositoryJSON implements PortfolioRepository {

    private static PortfolioRepositoryJSON instance;
    private final String FILE_PATH = "data/portfolio.json";

    private PortfolioRepositoryJSON(){}

    public static synchronized PortfolioRepositoryJSON getInstance() {
        if (instance == null)
            instance = new PortfolioRepositoryJSON();
        return instance;
    }

    private List<String> loadRaw() {
        try {
            File f = new File(FILE_PATH);
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                new FileWriter(f).write("{}");
            }

            BufferedReader br = new BufferedReader(new FileReader(f));
            StringBuilder sb = new StringBuilder();
            String line;

            while((line = br.readLine()) != null)
                sb.append(line);
            br.close();

            return Collections.singletonList(sb.toString());

        } catch (Exception e) {
            System.out.println("Error loading portfolio: " + e.getMessage());
            return Collections.singletonList("{}");
        }
    }

    @Override
    public List<Credit> getCreditsByBuyer(String email) {
        // You will add JSON parsing here — but for now:
        return new ArrayList<>();
    }

    @Override
    public void saveCreditForBuyer(String email, Credit credit) {
        // Save credit to buyer portfolio (JSON write)
    }
}
