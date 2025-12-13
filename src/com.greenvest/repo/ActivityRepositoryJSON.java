package com.greenvest.repo;

import com.greenvest.model.Activity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ActivityRepositoryJSON {

    private static ActivityRepositoryJSON instance;
    private final String FILE_PATH = "data/activity_log.json";

    private ActivityRepositoryJSON() {}

    public static synchronized ActivityRepositoryJSON getInstance() {
        if (instance == null) {
            instance = new ActivityRepositoryJSON();
        }
        return instance;
    }

    public void save(Activity activity) {

        List<Activity> list = loadAll();
        list.add(activity);
        writeAll(list);
    }

    public List<Activity> loadAll() {

        List<Activity> list = new ArrayList<>();

        try {
            File f = new File(FILE_PATH);
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                new FileWriter(f).write("[]");
            }

            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while ((line = br.readLine()) != null) {

                if (line.contains("message")) {
                    String msg = line.split("\"message\":\"")[1].split("\"")[0];
                    String user = line.split("\"performedBy\":\"")[1].split("\"")[0];
                    list.add(new Activity(msg, user));
                }
            }

        } catch (Exception e) {
            System.out.println("Error loading activity log");
        }

        return list;
    }

    private void writeAll(List<Activity> list) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {

            bw.write("[\n");
            for (int i = 0; i < list.size(); i++) {

                Activity a = list.get(i);

                bw.write(
                        "{\"message\":\"" + a.getMessage() +
                                "\",\"performedBy\":\"" + a.getPerformedBy() +
                                "\",\"time\":\"" + a.getTimestamp() + "\"}"
                );

                if (i < list.size() - 1) bw.write(",");
                bw.write("\n");
            }
            bw.write("]");

        } catch (Exception e) {
            System.out.println("Error saving activity log");
        }
    }
}
