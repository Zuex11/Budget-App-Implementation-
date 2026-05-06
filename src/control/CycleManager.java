package control;

import persistence.DatabaseHelper;

import domain.BudgetCycle;
import domain.CycleStatus;
import java.time.LocalDate;

/**
 * Handles the lifecycle of a {@link BudgetCycle}: creation, retrieval,
 * status updates, and deletion.
 *
 * <p>Acts as the intermediary between the application layer ({@code App})
 * and the persistence layer ({@link DatabaseHelper}) for all cycle-related
 * operations.</p>
 */
public class CycleManager {

    /** Persistence helper used for all cycle database operations. */
    private DatabaseHelper dbHelper;

    /**
     * Constructs a CycleManager backed by the singleton {@link DatabaseHelper}.
     */
    public CycleManager() {
        this.dbHelper = DatabaseHelper.getInstance();
    }

    /**
     * Creates and activates a new budget cycle, persisting it to the database.
     *
     * @param allowance total budget for the cycle in EGP
     * @param start     first day of the cycle
     * @param end       last day of the cycle
     * @return the newly created and activated {@link BudgetCycle}
     */
    public BudgetCycle initializeCycle(double allowance, LocalDate start, LocalDate end) {
        BudgetCycle cycle = new BudgetCycle(allowance, start, end);
        long id = dbHelper.insertCycle(cycle);
        cycle.setId((int) id);
        dbHelper.updateCycleStatus((int) id, CycleStatus.ACTIVE);
        cycle.setStatus(CycleStatus.ACTIVE);
        return cycle;
    }

    /**
     * Retrieves the current (most recent) budget cycle from the database.
     *
     * @return the active {@link BudgetCycle}, or {@code null} if none exists
     */
    public BudgetCycle getActiveCycle() {
        return dbHelper.getBudgetCycle();
    }

    /**
     * Permanently deletes the current cycle and all its associated data.
     * The cycle status is also set to {@link CycleStatus#ENDED} before deletion.
     */
    public void resetCycle() {
        BudgetCycle cycle = dbHelper.getBudgetCycle();
        dbHelper.deleteCycle(cycle.getId());
        dbHelper.updateCycleStatus(cycle.getId(), CycleStatus.ENDED);
    }

    /**
     * Marks the current cycle as {@link CycleStatus#ENDED} without deleting it.
     */
    public void endCycle() {
        BudgetCycle cycle = dbHelper.getBudgetCycle();
        dbHelper.updateCycleStatus(cycle.getId(), CycleStatus.ENDED);
    }

    /**
     * Updates the status of a given cycle in the database.
     *
     * @param cycle  the cycle whose status should be updated
     * @param status the new {@link CycleStatus} to assign
     */
    public void updateCycleStatus(BudgetCycle cycle, CycleStatus status) {
        dbHelper.updateCycleStatus(cycle.getId(), status);
    }
}
