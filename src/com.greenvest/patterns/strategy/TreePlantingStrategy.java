package com.greenvest.patterns.strategy;

/*
 * TreePlantingStrategy calculates credits
 * based on number of trees planted.
 */
public class TreePlantingStrategy implements CreditCalculationStrategy {

    // Calculates credits from tree count
    @Override
    public int calculateCredits(double metric) {

        // 1 credit for every 10 trees (rounded up)
        int credits = (int) Math.ceil(metric / 10.0);

        // Ensure at least one credit is given
        return Math.max(1, credits);
    }
}
