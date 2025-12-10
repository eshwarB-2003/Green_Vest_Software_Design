package com.greenvest.repo;

import com.greenvest.model.Credit;

import java.io.*;
import java.util.*;

public class CreditRepositoryJSON implements CreditRepository {

    private static CreditRepositoryJSON instance;
    private final String FILE_PATH = "data/credits.json";

    private CreditRepositoryJSON() {}

    public static synchronized CreditRepositoryJSON getInstance() {
        if (instance == null) instance = new CreditRepositoryJSON();
        return instance;
    }

    @Override
    public List<Credit> getAvailableCredits() {
        // load credits (similar manual JSON as users)
        return new ArrayList<>(); // (implemented by student)
    }

    @Override
    public void updateCredit(Credit c) {
        // update credit entry (implemented by student)
    }
}
