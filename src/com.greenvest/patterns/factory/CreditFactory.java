// credit factory used to create credit objects
// uses factory adn strategy ton calculate credits based on action type

package com.greenvest.patterns.factory;

import com.greenvest.model.Credit;
import com.greenvest.patterns.strategy.*;

import java.time.LocalDate;
import java.util.UUID;

public class CreditFactory {

    // create credit based on seller action type
    public static Credit createCredit(String sellerEmail,String type, double metricValue) {

        // select calculation strategy based on action type
        CreditCalculationStrategy strategy;
                switch (type) {
                    case "TREE" -> strategy = new TreePlantingStrategy();
                    case "SOLAR" -> strategy = new RenewableEnergyStrategy();
                    default -> throw new IllegalArgumentException( "Invalid action type: " + type );
                };

        // calculate credits using selected strategy
        int qty = strategy.calculateCredits(metricValue);

        // create and return new credit object
        return new Credit(
                UUID.randomUUID().toString(),
                sellerEmail,
                qty,
                10.0,
                LocalDate.now().plusYears(2)
        );
    }
}
