package com.greenvest.repo;
import com.greenvest.model.Receipt;

import java.io.*;
import java.util.*;

public class ReceiptRepositoryJSON implements ReceiptRepository {

    private static ReceiptRepositoryJSON instance;

    private final String FILE_PATH = "data/receipts.json";

    private ReceiptRepositoryJSON() {}

    public static synchronized ReceiptRepositoryJSON getInstance() {
        if (instance == null)
            instance = new ReceiptRepositoryJSON();
        return instance;
    }

    // ------------ Load All Receipts --------------
    private List<Receipt> loadAll() {
        List<Receipt> receipts = new ArrayList<>();

        try {
            File f = new File(FILE_PATH);

            if (!f.exists()) {
                f.getParentFile().mkdirs();
                f.createNewFile();
                new FileWriter(FILE_PATH).write("[]");
            }

            BufferedReader br = new BufferedReader(new FileReader(f));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null)
                sb.append(line);
            br.close();

            String raw = sb.toString().trim();
            if (raw.equals("") || raw.equals("[]")) return receipts;

            raw = raw.substring(1, raw.length() - 1); // remove []
            String[] entries = raw.split("\\},\\s*\\{");

            for (String entry : entries) {
                entry = entry.replace("{", "").replace("}", "");
                String[] pairs = entry.split(",");

                Map<String, String> map = new HashMap<>();

                for (String pair : pairs) {
                    String[] kv = pair.split(":");
                    if (kv.length == 2) {
                        String key = kv[0].replace("\"", "").trim();
                        String val = kv[1].replace("\"", "").trim();
                        map.put(key, val);
                    }
                }

                Receipt r = new Receipt(
                        map.get("id"),
                        map.get("creditId"),
                        Integer.parseInt(map.get("quantity")),
                        Double.parseDouble(map.get("totalCost")),
                        map.get("buyerEmail")
                );

                receipts.add(r);
            }

        } catch (Exception e) {
            System.out.println("Error loading receipts: " + e.getMessage());
        }

        return receipts;
    }

    // ------------ Save All Receipts --------------
    private void saveAll(List<Receipt> receipts) {
        try {
            File f = new File(FILE_PATH);
            f.getParentFile().mkdirs();

            BufferedWriter bw = new BufferedWriter(new FileWriter(f));

            bw.write("[\n");

            for (int i = 0; i < receipts.size(); i++) {
                Receipt r = receipts.get(i);

                bw.write("  {\n");
                bw.write("    \"id\": \"" + r.getId() + "\",\n");
                bw.write("    \"creditId\": \"" + r.getCreditId() + "\",\n");
                bw.write("    \"quantity\": \"" + r.getQuantity() + "\",\n");
                bw.write("    \"totalCost\": \"" + r.getTotalCost() + "\",\n");
                bw.write("    \"buyerEmail\": \"" + r.getBuyerEmail() + "\"\n");
                bw.write("  }" + (i < receipts.size() - 1 ? "," : "") + "\n");
            }

            bw.write("]");
            bw.close();

        } catch (Exception e) {
            System.out.println("Error saving receipts: " + e.getMessage());
        }
    }

    // ------------ Public Repository Methods --------------

    @Override
    public void saveReceipt(Receipt r) {
        List<Receipt> receipts = loadAll();
        receipts.add(r);
        saveAll(receipts);
    }

    @Override
    public List<Receipt> getReceiptsByBuyer(String buyerEmail) {
        List<Receipt> result = new ArrayList<>();

        for (Receipt r : loadAll()) {
            if (r.getBuyerEmail().equalsIgnoreCase(buyerEmail))
                result.add(r);
        }
        return result;
    }

    @Override
    public List<Receipt> getAllReceipts() {
        return loadAll();
    }
}

