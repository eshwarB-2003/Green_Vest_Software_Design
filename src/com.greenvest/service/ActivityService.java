package com.greenvest.service;

import com.greenvest.model.Activity;
import com.greenvest.repo.ActivityRepositoryJSON;

import java.util.List;

public class ActivityService {

    private ActivityRepositoryJSON repo =
            ActivityRepositoryJSON.getInstance();

    public void log(String message, String userEmail) {
        repo.save(new Activity(message, userEmail));
    }

    public List<Activity> getAllActivities() {
        return repo.loadAll();
    }
}
