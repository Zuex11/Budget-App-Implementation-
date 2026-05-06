package control;

import persistence.DatabaseHelper;
import domain.BudgetCycle;

/**
 * Calculates daily spending limits based on the remaining balance and
 * remaining days in the active {@link BudgetCycle}.
 *
 * <p>Formula: {@code dailyLimit = remainingBalance / remainingDays}</p>
 */
public class LimitCalculator {

    /** Persistence helper used to fetch spending totals and cycle data. */
    private DatabaseHelper dbHelper;

    /**
     * Constructs a LimitCalculator backed by the singleton {@link DatabaseHelper}.
     */
    public LimitCalculator() {
        this.dbHelper = DatabaseHelper.getInstance();
    }

    /**
     * Calculates the recommended daily spending limit for a cycle.
     * Returns 0 if the cycle has no remaining days.
     *
     * @param cycle the active {@link BudgetCycle} with an up-to-date
     *              {@code totalSpent} value
     * @return daily limit in EGP, or 0 if the cycle has ended
     */
    public double calculateDailyLimit(BudgetCycle cycle) {
        int remainingDays = cycle.getRemainingDays();
        if (remainingDays <= 0) {
            return 0;
        }
        return cycle.getRemainingBalance() / remainingDays;
    }

    /**
     * Queries the database for the cumulative amount spent in a cycle.
     *
     * @param cycleId the cycle to query
     * @return total spent in EGP
     */
    public double calculateSpentTotal(int cycleId) {
        return dbHelper.getTotalSpent(cycleId);
    }

    /**
     * Fetches the latest spending total, updates the cycle, and recalculates
     * the daily limit. Called after every new expense is logged.
     *
     * @param cycleId the cycle to recalculate for
     * @return updated daily limit in EGP
     */
    public double recalculate(int cycleId) {
        double totalSpent = calculateSpentTotal(cycleId);
        BudgetCycle cycle = dbHelper.getBudgetCycle();
        cycle.setTotalSpent(totalSpent);
        return calculateDailyLimit(cycle);
    }
}
