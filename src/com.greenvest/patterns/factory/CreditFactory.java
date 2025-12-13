package com.greenvest.patterns.factory;


import com.greenvest.model.Credit;
import com.greenvest.patterns.strategy.*;

import java.time.LocalDate;
import java.util.UUID;

public class CreditFactory {

    public static Credit createCredit(String sellerEmail,String type, double metricValue) {

        CreditCalculationStrategy strategy =
                switch (type) {
                    case "TREE" -> strategy = new TreePlantingStrategy();
                    case "SOLAR" -> strategy = new RenewableEnergyStrategy();
                    default -> throw new IllegalArgumentException(
                            "Invalid action type: " + type
                    );
                };

        int qty = strategy.calculateCredits(metricValue);

        return new Credit(
                UUID.randomUUID().toString(),
                sellerEmail,
                qty,
                10.0,
                LocalDate.now().plusYears(2)
        );
    }
}
