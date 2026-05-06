package control;

import domain.*;
import persistence.DatabaseHelper;

import java.util.List;

/**
 * Coordinates expense and category operations, delegating persistence to
 * {@link DatabaseHelper} and triggering budget alerts via {@link AlertManager}.
 *
 * <p>This is the primary control-layer entry point for anything related to
 * logging, editing, or deleting expenses, as well as managing categories.</p>
 */
public class ExpenseManager {

    /** Persistence helper for all database operations. */
    private DatabaseHelper dbHelper;

    /** Responsible for checking budget thresholds after each expense. */
    private AlertManager alertManager;

    /** Recalculates the daily spending limit after each expense. */
    private LimitCalculator limitCalculator;

    /**
     * Constructs an ExpenseManager and initialises its collaborators.
     */
    public ExpenseManager() {
        this.dbHelper = DatabaseHelper.getInstance();
        this.alertManager = new AlertManager();
        this.limitCalculator = new LimitCalculator();
    }

    /**
     * Logs a new expense, persists it, recalculates the daily limit, and
     * checks budget thresholds.
     *
     * @param amount     monetary value in EGP (must be &gt; 0)
     * @param categoryId ID of the category to assign
     * @param cycleId    ID of the active budget cycle
     * @return the persisted {@link Expense} with its generated ID set
     */
    public Expense logExpense(double amount, int categoryId, int cycleId) {
        Expense expense = new Expense(amount, categoryId, cycleId);
        long id = dbHelper.insertExpense(expense);
        expense.setId((int) id);

        double newDailyLimit = limitCalculator.recalculate(cycleId);

        BudgetCycle cycle = dbHelper.getBudgetCycle();
        double totalSpent = limitCalculator.calculateSpentTotal(cycleId);
        cycle.setTotalSpent(totalSpent);

        alertManager.checkThreshold(cycle);

        return expense;
    }

    /**
     * Updates the amount and category of an existing expense.
     *
     * @param id         database ID of the expense to edit
     * @param amount     new monetary value in EGP
     * @param categoryId new category ID
     */
    public void editExpense(int id, double amount, int categoryId) {
        Expense expense = new Expense(amount, categoryId, 0);
        expense.setId(id);
        dbHelper.updateExpense(expense);
    }

    /**
     * Permanently deletes an expense record from the database.
     *
     * @param id database ID of the expense to delete
     */
    public void deleteExpense(int id) {
        dbHelper.deleteExpense(id);
    }

    /**
     * Returns all expenses belonging to a given budget cycle.
     *
     * @param cycleId the cycle to query
     * @return list of {@link Expense} objects; empty if none found
     */
    public List<Expense> getExpensesByCycle(int cycleId) {
        return dbHelper.getAllExpenses(cycleId);
    }

    /**
     * Returns all expenses for a specific category within a cycle.
     *
     * @param cycleId    the cycle to query
     * @param categoryId the category to filter by
     * @return filtered list of {@link Expense} objects
     */
    public List<Expense> getExpensesByCategory(int cycleId, int categoryId) {
        return dbHelper.getExpensesByCategory(cycleId, categoryId);
    }

    /**
     * Creates and persists a new expense category.
     *
     * @param name      display name for the category
     * @param iconResId icon resource ID (pass 0 if none)
     * @return the newly created {@link Category} with its generated ID set
     */
    public Category insertCategory(String name, int iconResId) {
        Category category = new Category(0, name, iconResId);
        long id = dbHelper.insertCategory(category);
        category = new Category((int) id, name, iconResId);
        return category;
    }

    /**
     * Returns all categories stored in the database.
     *
     * @return list of all {@link Category} objects
     */
    public List<Category> getCategories() {
        return dbHelper.getAllCategories();
    }

    /**
     * Returns the total amount spent today within the given cycle.
     *
     * @param cycleId the cycle to query
     * @return today's total spending in EGP
     */
    public double getTodaySpent(int cycleId) {
        return dbHelper.getTodaySpent(cycleId);
    }

    /**
     * Permanently deletes a category from the database.
     *
     * @param id database ID of the category to delete
     */
    public void deleteCategory(int id) {
        dbHelper.deleteCategory(id);
    }
}
