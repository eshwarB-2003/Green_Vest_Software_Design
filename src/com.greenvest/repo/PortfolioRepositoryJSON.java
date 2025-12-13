/*
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
        List<Credit> credits = getCreditsByBuyer(email);
        credits.add(credit);
        writeAll(email, credits);
    }
}
*/
package com.greenvest.repo;

import com.greenvest.model.Credit;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class PortfolioRepositoryJSON implements PortfolioRepository {

    private static PortfolioRepositoryJSON instance;
    private final String DIR = "data/portfolios/";

    private PortfolioRepositoryJSON() {}

    public static synchronized PortfolioRepositoryJSON getInstance() {
        if (instance == null) {
            instance = new PortfolioRepositoryJSON();
        }
        return instance;
    }

    /* ---------------- SAVE CREDIT TO BUYER PORTFOLIO ---------------- */

    @Override
    public void saveCreditForBuyer(String buyerEmail, Credit credit) {

        List<Credit> credits = getCreditsByBuyer(buyerEmail);
        credits.add(credit);
        writeAll(buyerEmail, credits);
    }

    /* ---------------- LOAD BUYER PORTFOLIO ---------------- */

    @Override
    public List<Credit> getCreditsByBuyer(String buyerEmail) {

        List<Credit> list = new ArrayList<>();
        File file = new File(DIR + buyerEmail + ".json");

        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                new FileWriter(file).write("[]");
                return list;
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();

            String json = sb.toString().trim();
            if (json.equals("[]") || json.length() <= 2) return list;

            if (json.startsWith("[")) json = json.substring(1);
            if (json.endsWith("]")) json = json.substring(0, json.length() - 1);

            String[] items = json.split("\\},\\s*\\{");

            for (String s : items) {

                s = s.replace("{", "").replace("}", "");
                String[] kvs = s.split(",");

                Map<String, String> map = new HashMap<>();
                for (String kv : kvs) {
                    String[] pair = kv.split(":");
                    map.put(pair[0].replace("\"", ""),
                            pair[1].replace("\"", ""));
                }

                Credit c = new Credit(
                        map.get("id"),
                        map.get("sellerEmail"),
                        Integer.parseInt(map.get("quantity")),
                        Double.parseDouble(map.get("price")),
                        LocalDate.parse(map.get("expiry"))
                );

                list.add(c);
            }

        } catch (Exception e) {
            System.out.println("Error loading portfolio: " + e.getMessage());
        }

        return list;
    }

    /* ---------------- WRITE BUYER PORTFOLIO ---------------- */

    private void writeAll(String buyerEmail, List<Credit> list) {

        File file = new File(DIR + buyerEmail + ".json");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {

            bw.write("[\n");
            for (int i = 0; i < list.size(); i++) {
                Credit c = list.get(i);

                bw.write("{\"id\":\"" + c.getId() +
                        "\",\"sellerEmail\":\"" + c.getSellerEmail() +
                        "\",\"quantity\":\"" + c.getQuantity() +
                        "\",\"price\":\"" + c.getPrice() +
                        "\",\"expiry\":\"" + c.getExpiry() + "\"}");

                if (i < list.size() - 1) bw.write(",");
                bw.write("\n");
            }
            bw.write("]");

        } catch (Exception e) {
            System.out.println("Error writing portfolio: " + e.getMessage());
        }
    }
}
