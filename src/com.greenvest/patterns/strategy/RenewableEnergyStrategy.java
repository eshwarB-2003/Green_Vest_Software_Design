package com.greenvest.patterns.strategy;

/*
 * RenewableEnergyStrategy calculates credits
 * based on energy generated in kWh.
 */
public class RenewableEnergyStrategy implements CreditCalculationStrategy {

    // Calculates credits from kWh value
    @Override
    public int calculateCredits(double kwh) {

        // Invalid energy gives no credits
        if (kwh <= 0) return 0;

        // 1 credit for every 100 kWh (rounded up)
        int credits = (int) Math.ceil(kwh / 100.0);

        // Ensure at least one credit is generated
        return Math.max(1, credits);
    }
}
