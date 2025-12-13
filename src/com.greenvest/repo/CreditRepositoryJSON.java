package com.greenvest.repo;

import com.greenvest.model.Credit;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class CreditRepositoryJSON implements CreditRepository {

    private static CreditRepositoryJSON instance;
    private final String FILE_PATH = "data/seller_credits.json";

    private CreditRepositoryJSON() {}

    public static synchronized CreditRepositoryJSON getInstance() {
        if (instance == null)
            instance = new CreditRepositoryJSON();
        return instance;
    }

    @Override
    public void save(Credit credit) {
        List<Credit> credits = getAll();
        credits.add(credit);
        writeAll(credits);
    }

    @Override
    public List<Credit> getAvailableCredits() {
        List<Credit> result = new ArrayList<>();
        for (Credit c : getAll()) {
            if (!c.isExpired()) result.add(c);
        }
        return result;
    }

    @Override
    public List<Credit> getBySeller(String sellerEmail) {
        List<Credit> list = new ArrayList<>();
        for (Credit c : getAll()) {
            if (c.getSellerEmail().equalsIgnoreCase(sellerEmail))
                list.add(c);
        }
        return list;
    }

    @Override
    public void updateCredit(Credit credit) {
        List<Credit> credits = getAll();
        for (int i = 0; i < credits.size(); i++) {
            if (credits.get(i).getId().equals(credit.getId())) {
                credits.set(i, credit);
                break;
            }
        }
        writeAll(credits);
    }

    /* ---------- INTERNAL JSON HELPERS ---------- */

    private List<Credit> getAll() {

        List<Credit> list = new ArrayList<>();

        try {
            File f = new File(FILE_PATH);

            if (!f.exists()) {
                f.getParentFile().mkdirs();
                new FileWriter(f).write("[]");
            }

            BufferedReader br = new BufferedReader(new FileReader(f));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();

            String json = sb.toString().trim();

            if (json.equals("[]") || json.length() <= 2) {
                return list;
            }

            if (json.startsWith("[")) json = json.substring(1);
            if (json.endsWith("]")) json = json.substring(0, json.length() - 1);

            if (json.trim().isEmpty()) return list;

            String[] items = json.split("\\},\\s*\\{");

            for (String s : items) {

                s = s.replace("{", "").replace("}", "");
                String[] kvs = s.split(",");
                String state = "ACTIVE";
                boolean listed = false;
                Map<String, String> map = new HashMap<>();
                for (String kv : kvs) {
                    String[] pair = kv.split(":");
                    String key = pair[0].replace("\"", "");
                    String val = pair[1].replace("\"", "");

                    switch (key) {
                        case "id" -> map.put("id", val);
                        case "sellerEmail" -> map.put("sellerEmail", val);
                        case "quantity" -> map.put("quantity", val);
                        case "price" -> map.put("price", val);
                        case "expiry" -> map.put("expiry", val);
                        case "state" -> state = val;
                        case "listed" -> listed = Boolean.parseBoolean(val);
                    }
                }


                Credit c = new Credit(
                        map.get("id"),
                        map.get("sellerEmail"),
                        Integer.parseInt(map.get("quantity")),
                        Double.parseDouble(map.get("price")),
                        LocalDate.parse(map.get("expiry"))
                );
                if ("EXPIRED".equals(state)) {
                    c.updateState();
                }

                if (listed) {
                    c.list();
                }

                list.add(c);
            }

        } catch (Exception e) {
            System.out.println("Error reading credits: " + e.getMessage());
        }

        return list;
    }

    /*private void writeAll(List<Credit> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            bw.write("[\n");
            for (int i = 0; i < list.size(); i++) {
                Credit c = list.get(i);
                bw.write("{\"id\":\"" + c.getId() +
                        "\",\"sellerEmail\":\"" + c.getSellerEmail() +
                        "\",\"quantity\":\"" + c.getQuantity() +
                        "\",\"price\":\"" + c.getPrice() +
                        "\",\"expiry\":\"" + c.getExpiry() +
                         "\",\"state\":\"" + c.getState() +
                        ",\"listed\":\"" + c.isListed() + "\"}");
                if (i < list.size() - 1) bw.write(",");
                bw.write("\n");
            }
            bw.write("]");
        } catch (Exception e) {
            System.out.println("Error writing credits: " + e.getMessage());
        }
    }
     */
    private void writeAll(List<Credit> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {

            bw.write("[\n");
            for (int i = 0; i < list.size(); i++) {
                Credit c = list.get(i);

                bw.write(
                        "{"
                                + "\"id\":\"" + c.getId() + "\","
                                + "\"sellerEmail\":\"" + c.getSellerEmail() + "\","
                                + "\"quantity\":\"" + c.getQuantity() + "\","
                                + "\"price\":\"" + c.getPrice() + "\","
                                + "\"expiry\":\"" + c.getExpiry() + "\","
                                + "\"state\":\"" + c.getState() + "\","
                                + "\"listed\":\"" + c.isListed() + "\""
                                + "}"
                );

                if (i < list.size() - 1) bw.write(",");
                bw.write("\n");
            }

            bw.write("]");

        } catch (Exception e) {
            System.out.println("Error writing credits: " + e.getMessage());
        }
    }
}
