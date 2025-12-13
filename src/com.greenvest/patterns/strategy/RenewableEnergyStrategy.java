package com.greenvest.patterns.strategy;
public class RenewableEnergyStrategy implements CreditCalculationStrategy {

    @Override
    public int calculateCredits(double kwh) {

        if (kwh <= 0) return 0;

        int credits = (int) Math.ceil(kwh / 100.0);

        return Math.max(1, credits);
    }
}
