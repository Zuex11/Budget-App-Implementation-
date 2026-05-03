package control;

import persistence.DatabaseHelper;
import domain.*;

public class AlertManager {

    private DatabaseHelper dbHelper;

    public AlertManager() {
        this.dbHelper = DatabaseHelper.getInstance();
    }

    public void sendNotification(String message) {

        System.out.println("ALERT: " + message);  // replace system output with actual notification 

    }

    public boolean hasAlertBeenSent(int cycleId) {
        BudgetCycle cycle = dbHelper.getBudgetCycle();
        if (cycle == null) {
            return false;
        }
        CycleStatus status = cycle.getStatus();
        return status == CycleStatus.NEAR_LIMIT || status == CycleStatus.EXHAUSTED;

    }

    public void checkThreshold(BudgetCycle cycle) {
        double spentPercentage = cycle.getSpentPercentage();
        if (spentPercentage >= 100) {
            dbHelper.updateCycleStatus(cycle.getId(), CycleStatus.EXHAUSTED);
            sendNotification("Budget Exhausted. No funds remaining.");
        } else if (spentPercentage >= 80 && !hasAlertBeenSent(cycle.getId())) {
            dbHelper.updateCycleStatus(cycle.getId(), CycleStatus.NEAR_LIMIT);
            sendNotification("Warning: You have used 80% of your allowance.");
        }
    }


}