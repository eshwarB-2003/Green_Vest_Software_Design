/*package com.greenvest.repo;

import com.greenvest.model.SustainabilityAction;

import java.io.*;
import java.util.*;

public class ActionRepositoryJSON implements ActionRepository {

    private static ActionRepositoryJSON instance;
    private final String FILE_PATH = "data/actions.json";

    private ActionRepositoryJSON() {}

    public static synchronized ActionRepositoryJSON getInstance() {
        if (instance == null)
            instance = new ActionRepositoryJSON();
        return instance;
    }

    @Override
    public void save(SustainabilityAction action) {
        List<SustainabilityAction> actions = loadAll();
        actions.add(action);
        writeAll(list);
    }

    @Override
   /* public List<SustainabilityAction> getPendingActions() {
        List<SustainabilityAction> list = new ArrayList<>();

    public List<SustainabilityAction> getPendingActions() {

        List<SustainabilityAction> all = loadAll();
        List<SustainabilityAction> pending = new ArrayList<>();

        for (SustainabilityAction a : all) {
            if (!a.isApproved()) {        // 👈 ONLY PENDING
                pending.add(a);
            }
        }
        return pending;
    }

        try {
            File f = new File(FILE_PATH);
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                new FileWriter(f).write("[]");
            }

            BufferedReader br = new BufferedReader(new FileReader(f));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null)
                sb.append(line);
            br.close();

            String raw = sb.toString();
            if (raw.equals("[]")) return list;

            raw = raw.substring(1, raw.length() - 1);
            String[] entries = raw.split("\\},\\{");

            for (String e : entries) {
                e = e.replace("{", "").replace("}", "");
                String[] parts = e.split(",");

                String id="", seller="", type="";
                double metric=0;
                boolean approved=false;

                for (String p : parts) {
                    String[] kv = p.split(":");
                    String key = kv[0].replace("\"","");
                    String val = kv[1].replace("\"","");

                    switch (key) {
                        case "id" -> id = val;
                        case "sellerEmail" -> seller = val;
                        case "type" -> type = val;
                        case "metricValue" -> metric = Double.parseDouble(val);
                        case "approved" -> approved = Boolean.parseBoolean(val);
                    }
                }

                SustainabilityAction a =
                        new SustainabilityAction(id, seller, type, metric);
                if (approved) a.approve();

                list.add(a);
            }
        } catch (Exception ignored) {}

        return list;
    }

    @Override
    public void update(SustainabilityAction action) {
        List<SustainabilityAction> list = getPendingActions();
        writeAll(list);
    }

    private void writeAll(List<SustainabilityAction> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            bw.write("[\n");
            for (int i=0;i<list.size();i++) {
                SustainabilityAction a = list.get(i);
                bw.write("{\"id\":\""+a.getId()+"\",\"sellerEmail\":\""+
                        a.getSellerEmail()+"\",\"type\":\""+a.getType()+
                        "\",\"metricValue\":\""+a.getMetricValue()+
                        "\",\"approved\":\""+a.isApproved()+"\"}");
                if (i<list.size()-1) bw.write(",");
                bw.write("\n");
            }
            bw.write("]");
        } catch (Exception ignored) {}
    }
}
*/
package com.greenvest.repo;

import com.greenvest.model.SustainabilityAction;

import java.io.*;
import java.util.*;

public class ActionRepositoryJSON implements ActionRepository {

    private static ActionRepositoryJSON instance;
    private final String FILE_PATH = "data/actions.json";

    private ActionRepositoryJSON() {}

    public static synchronized ActionRepositoryJSON getInstance() {
        if (instance == null) {
            instance = new ActionRepositoryJSON();
        }
        return instance;
    }
    @Override
    public List<SustainabilityAction> getApprovedActions() {
        return loadAll().stream()
                .filter(a -> a.getStatus().equals("Approved"))
                .toList();
    }

    @Override
    public List<SustainabilityAction> getRejectedActions() {
        return loadAll().stream()
                .filter(a -> a.getStatus().equals("Rejected"))
                .toList();
    }


    // ---------------- SAVE ----------------
    @Override
    public void save(SustainabilityAction action) {
        List<SustainabilityAction> actions = loadAll();
       List<SustainabilityAction> pending = new ArrayList<>();

       actions.add(action);
        writeAll(actions);
    }

    // ---------------- GET PENDING ----------------
    @Override
    public List<SustainabilityAction> getPendingActions() {

        List<SustainabilityAction> all = loadAll();
        List<SustainabilityAction> pending = new ArrayList<>();

        for (SustainabilityAction a : all) {
            if (a.isPending()) {     // ✅ STATUS-BASED
                pending.add(a);
            }
        }
        return pending;
    }

    // ---------------- UPDATE ----------------
    @Override
    public void update(SustainabilityAction action) {

        List<SustainabilityAction> all = loadAll();

        for (SustainabilityAction a : all) {
            if (a.getId().equals(action.getId())) {

                // 🔥 FORCE STATUS OVERWRITE
                if (action.getStatus().equals("Approved")) {
                    a.approve();
                }
                else if (action.getStatus().equals("Rejected")) {
                    a.reject();
                }

                break;
            }
        }

        writeAll(all);
    }

    // ---------------- LOAD ALL ----------------
    private List<SustainabilityAction> loadAll() {

        List<SustainabilityAction> list = new ArrayList<>();

        try {
            File file = new File(FILE_PATH);

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

            if (json.startsWith("[")) json = json.substring(1);
            if (json.endsWith("]")) json = json.substring(0, json.length() - 1);

            if (json.trim().isEmpty()) return list;

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

    // ---------------- WRITE ALL ----------------
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
