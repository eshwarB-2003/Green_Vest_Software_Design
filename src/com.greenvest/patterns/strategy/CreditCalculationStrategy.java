package com.greenvest.patterns.strategy;

/*
 * CreditCalculationStrategy defines
 * how credits are calculated for
 * different sustainability actions.
 */
public interface CreditCalculationStrategy {

    // Calculates credits based on given metric value
    int calculateCredits(double metricValue);
}
