package control;

import persistence.DatabaseHelper;
import domain.*;

/**
 * Monitors budget thresholds and sends notifications when spending milestones
 * are reached.
 *
 * <p>Two thresholds are enforced:
 * <ul>
 *   <li><b>80 %</b> – cycle status set to {@link CycleStatus#NEAR_LIMIT} and
 *       a warning notification is sent (once per cycle).</li>
 *   <li><b>100 %</b> – cycle status set to {@link CycleStatus#EXHAUSTED} and
 *       a critical notification is sent.</li>
 * </ul>
 */
public class AlertManager {

    /** Persistence helper used to read and update cycle status. */
    private DatabaseHelper dbHelper;

    /**
     * Constructs an AlertManager backed by the singleton {@link DatabaseHelper}.
     */
    public AlertManager() {
        this.dbHelper = DatabaseHelper.getInstance();
    }

    /**
     * Sends a notification message. Currently prints to stdout; intended to be
     * replaced with a real notification mechanism (e.g., system tray alert).
     *
     * @param message the notification text to display
     */
    public void sendNotification(String message) {
        System.out.println("ALERT: " + message);
    }

    /**
     * Checks whether a threshold alert has already been sent for a cycle by
     * inspecting its persisted status.
     *
     * @param cycleId the cycle ID to check (currently unused; reads from DB directly)
     * @return {@code true} if the cycle status is {@link CycleStatus#NEAR_LIMIT}
     *         or {@link CycleStatus#EXHAUSTED}
     */
    public boolean hasAlertBeenSent(int cycleId) {
        BudgetCycle cycle = dbHelper.getBudgetCycle();
        if (cycle == null) {
            return false;
        }
        CycleStatus status = cycle.getStatus();
        return status == CycleStatus.NEAR_LIMIT || status == CycleStatus.EXHAUSTED;
    }

    /**
     * Evaluates the cycle's spent percentage and fires the appropriate alert.
     * The 80 % warning is only sent once per cycle; the 100 % alert fires every
     * time spending reaches or exceeds the allowance.
     *
     * @param cycle the {@link BudgetCycle} to evaluate (must have {@code totalSpent} set)
     */
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
