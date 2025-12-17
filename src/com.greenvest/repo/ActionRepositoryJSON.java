package com.greenvest.repo;

import com.greenvest.model.SustainabilityAction;

import java.io.*;
import java.util.*;

/*
 * ActionRepositoryJSON stores sustainability actions
 * in a JSON file using simple file handling.
 * It follows the Singleton pattern.
 */
public class ActionRepositoryJSON implements ActionRepository {

    // Single instance of repository
    private static ActionRepositoryJSON instance;

    // File path for storing actions
    private final String FILE_PATH = "data/actions.json";

    // Private constructor for singleton
    private ActionRepositoryJSON() {}

    // Returns the single repository instance
    public static synchronized ActionRepositoryJSON getInstance() {
        if (instance == null) {
            instance = new ActionRepositoryJSON();
        }
        return instance;
    }

    // Returns all approved actions
    @Override
    public List<SustainabilityAction> getApprovedActions() {
        return loadAll().stream()
                .filter(a -> a.getStatus().equals("Approved"))
                .toList();
    }

    // Returns all rejected actions
    @Override
    public List<SustainabilityAction> getRejectedActions() {
        return loadAll().stream()
                .filter(a -> a.getStatus().equals("Rejected"))
                .toList();
    }

    /*
     * Saves a new sustainability action
     * to the JSON file.
     */
    @Override
    public void save(SustainabilityAction action) {
        List<SustainabilityAction> actions = loadAll();
        actions.add(action);
        writeAll(actions);
    }

    /*
     * Returns all actions that are
     * still pending approval.
     */
    @Override
    public List<SustainabilityAction> getPendingActions() {

        List<SustainabilityAction> all = loadAll();
        List<SustainabilityAction> pending = new ArrayList<>();

        for (SustainabilityAction a : all) {
            if (a.isPending()) {
                pending.add(a);
            }
        }
        return pending;
    }

    /*
     * Updates the status of an existing action
     * (Approved or Rejected).
     */
    @Override
    public void update(SustainabilityAction action) {

        List<SustainabilityAction> all = loadAll();

        for (SustainabilityAction a : all) {
            if (a.getId().equals(action.getId())) {

                // Update status based on admin decision
                if (action.getStatus().equals("Approved")) {
                    a.approve();
                } else if (action.getStatus().equals("Rejected")) {
                    a.reject();
                }
                break;
            }
        }

        writeAll(all);
    }

    /*
     * Loads all sustainability actions
     * from the JSON file.
     */
    private List<SustainabilityAction> loadAll() {

        List<SustainabilityAction> list = new ArrayList<>();

        try {
            File file = new File(FILE_PATH);

            // Create file if it does not exist
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                new FileWriter(file).write("[]");
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
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

            // Remove array brackets
            if (json.startsWith("[")) json = json.substring(1);
            if (json.endsWith("]")) json = json.substring(0, json.length() - 1);

            String[] entries = json.split("\\},\\s*\\{");

            for (String e : entries) {

                e = e.replace("{", "").replace("}", "");
                String[] parts = e.split(",");

                String id = "", seller = "", type = "", status = "Pending";
                double metric = 0;

                for (String p : parts) {
                    String[] kv = p.split(":");
                    String key = kv[0].replace("\"", "");
                    String val = kv[1].replace("\"", "");

                    switch (key) {
                        case "id" -> id = val;
                        case "sellerEmail" -> seller = val;
                        case "type" -> type = val;
                        case "metricValue" -> metric = Double.parseDouble(val);
                        case "status" -> status = val;
                    }
                }

                SustainabilityAction action =
                        new SustainabilityAction(id, seller, type, metric);

                if (status.equals("Approved")) action.approve();
                if (status.equals("Rejected")) action.reject();

                list.add(action);
            }

        } catch (Exception e) {
            System.out.println("Error loading actions: " + e.getMessage());
        }

        return list;
    }

    /*
     * Writes all sustainability actions
     * back to the JSON file.
     */
    private void writeAll(List<SustainabilityAction> list) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {

            bw.write("[\n");
            for (int i = 0; i < list.size(); i++) {
                SustainabilityAction a = list.get(i);

                bw.write("{\"id\":\"" + a.getId() +
                        "\",\"sellerEmail\":\"" + a.getSellerEmail() +
                        "\",\"type\":\"" + a.getType() +
                        "\",\"metricValue\":\"" + a.getMetricValue() +
                        "\",\"status\":\"" + a.getStatus() + "\"}");

                if (i < list.size() - 1) bw.write(",");
                bw.write("\n");
            }
            bw.write("]");

        } catch (Exception e) {
            System.out.println("Error writing actions: " + e.getMessage());
        }
    }
}
