package control;

import persistence.DatabaseHelper;

import domain.BudgetCycle;
import domain.CycleStatus;
import java.time.LocalDate;


public class CycleManager {
    private DatabaseHelper dbHelper;

    CycleManager() {
        this.dbHelper = DatabaseHelper.getInstance();
    }

    public BudgetCycle initializeCycle(double allowance, LocalDate start, LocalDate end) {
        BudgetCycle cycle = new BudgetCycle(allowance, start, end);
        long id = dbHelper.insertCycle(cycle);
        cycle.setId((int) id);
        dbHelper.updateCycleStatus((int)id, CycleStatus.ACTIVE);
        cycle.setStatus(CycleStatus.ACTIVE);
        return cycle;
    }

    public BudgetCycle getActiveCycle() {
        return dbHelper.getBudgetCycle();
    }

    public void resetCycle() {
        BudgetCycle cycle = dbHelper.getBudgetCycle();
        dbHelper.deleteCycle(cycle.getId());
        dbHelper.updateCycleStatus(cycle.getId(), CycleStatus.ENDED);
    }

    public void endCycle() {
        BudgetCycle cycle = dbHelper.getBudgetCycle();
        dbHelper.updateCycleStatus(cycle.getId(), CycleStatus.ENDED);
    }

    public void updateCycleStatus(BudgetCycle cycle, CycleStatus status) {
        dbHelper.updateCycleStatus(cycle.getId(), status);
    }
}