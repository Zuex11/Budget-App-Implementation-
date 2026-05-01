package control;

import persistence.DatabaseHelper;
import domain.BudgetCycle;


public class LimitCalculator {
     private DatabaseHelper dbHelper;

    LimitCalculator() {
        this.dbHelper = DatabaseHelper.getInstance();
    }

    public double calculateDailyLimit(BudgetCycle cycle) {
        int remainingDays = cycle.getRemainingDays();
        if (remainingDays <= 0) {
            return 0;
        }
        return cycle.getRemainingBalance() / remainingDays;

    }

    public double calculateSpentTotal( int cycleId) {
        return dbHelper.getTotalSpent(cycleId);
    }

    public double recalculate(int cycleId) {
        double totalSpent = calculateSpentTotal(cycleId);
        BudgetCycle cycle = dbHelper.getBudgetCycle();
        cycle.setTotalSpent(totalSpent);
        return calculateDailyLimit(cycle);
    }
    
}