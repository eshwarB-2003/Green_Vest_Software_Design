package com.greenvest.patterns.strategy;

public class TreePlantingStrategy implements CreditCalculationStrategy {
    @Override
    public int calculateCredits(double metric) {

        int credits = (int) Math.ceil(metric / 10.0);

        return Math.max(1, credits);   //
    }

}
