package app;

import com.formdev.flatlaf.FlatDarkLaf;

import control.AuthManager;
import control.CycleManager;
import control.ExpenseManager;
import control.LimitCalculator;
import domain.BudgetCycle;
import domain.Expense;
import ui.*;
import domain.Category;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Central application controller and facade for the Mizan budget app.
 *
 * <p>Owns the main {@link JFrame} and all control-layer managers. Every UI
 * screen holds a reference to this class and calls its methods to perform
 * business logic or navigate between screens. This keeps the UI screens
 * decoupled from the control and persistence layers.</p>
 *
 * <p>Startup flow:</p>
 * <ol>
 *   <li>If a PIN is set → show {@link LockScreen}.</li>
 *   <li>Else if no active cycle exists → show {@link SetupScreen}.</li>
 *   <li>Otherwise → show {@link DashboardScreen}.</li>
 * </ol>
 */
public class App {

    /** The single application window. */
    private JFrame frame;

    /** Manages budget cycle lifecycle (create, reset, end). */
    private CycleManager cycleManager;

    /** Manages expense and category CRUD operations. */
    private ExpenseManager expenseManager;

    /** Calculates and recalculates the daily spending limit. */
    private LimitCalculator limitCalculator;

    /** Handles PIN authentication and lockout logic. */
    private AuthManager authManager;

    /**
     * Constructs the App, initialises all managers, applies the dark theme,
     * creates the main window, and routes to the correct startup screen.
     */
    App() {
        cycleManager = new CycleManager();
        expenseManager = new ExpenseManager();
        limitCalculator = new LimitCalculator();
        authManager = new AuthManager();

        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame("Mizan");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setVisible(true);

        if (authManager.hasPin()) {
            showLockScreen();
        } else if (cycleManager.getActiveCycle() == null) {
            showSetupScreen();
        } else {
            showDashboardScreen();
        }
    }

    // -------------------------------------------------------------------------
    // Cycle delegation
    // -------------------------------------------------------------------------

    /**
     * Returns the current active budget cycle from the database.
     *
     * @return active {@link BudgetCycle}, or {@code null} if none exists
     */
    public BudgetCycle getActiveCycle() {
        return cycleManager.getActiveCycle();
    }

    /**
     * Creates and activates a new budget cycle.
     *
     * @param allowance total budget in EGP
     * @param start     cycle start date
     * @param end       cycle end date
     * @return the newly created {@link BudgetCycle}
     */
    public BudgetCycle initializeCycle(double allowance, LocalDate start, LocalDate end) {
        return cycleManager.initializeCycle(allowance, start, end);
    }

    /**
     * Permanently deletes the current cycle and all its expense data.
     */
    public void resetCycle() {
        cycleManager.resetCycle();
    }

    // -------------------------------------------------------------------------
    // Expense delegation
    // -------------------------------------------------------------------------

    /**
     * Logs a new expense under the active cycle.
     *
     * @param amount     monetary value in EGP
     * @param categoryId ID of the category to assign
     * @return the persisted {@link Expense} with its generated ID set
     */
    public Expense logExpense(double amount, int categoryId) {
        return expenseManager.logExpense(amount, categoryId, cycleManager.getActiveCycle().getId());
    }

    /**
     * Returns all expenses for the active cycle.
     *
     * @return list of {@link Expense} objects
     */
    public List<Expense> getExpenses() {
        return expenseManager.getExpensesByCycle(cycleManager.getActiveCycle().getId());
    }

    /**
     * Returns all expenses for a specific category in the active cycle.
     *
     * @param categoryId the category to filter by
     * @return filtered list of {@link Expense} objects
     */
    public List<Expense> getExpensesByCategory(int categoryId) {
        return expenseManager.getExpensesByCategory(cycleManager.getActiveCycle().getId(), categoryId);
    }

    /**
     * Updates the amount and category of an existing expense.
     *
     * @param id         database ID of the expense to edit
     * @param amount     new amount in EGP
     * @param categoryId new category ID
     */
    public void editExpense(int id, double amount, int categoryId) {
        expenseManager.editExpense(id, amount, categoryId);
    }

    /**
     * Permanently deletes an expense record.
     *
     * @param id database ID of the expense to delete
     */
    public void deleteExpense(int id) {
        expenseManager.deleteExpense(id);
    }

    /**
     * Returns the total amount spent today in the active cycle.
     *
     * @return today's spending in EGP
     */
    public double getTodaySpent() {
        return expenseManager.getTodaySpent(cycleManager.getActiveCycle().getId());
    }

    // -------------------------------------------------------------------------
    // Category delegation
    // -------------------------------------------------------------------------

    /**
     * Returns all expense categories.
     *
     * @return list of all {@link Category} objects
     */
    public List<Category> getCategories() {
        return expenseManager.getCategories();
    }

    /**
     * Creates and persists a new expense category with no icon.
     *
     * @param name display name for the new category
     * @return the newly created {@link Category} with its generated ID set
     */
    public Category insertCategory(String name) {
        return expenseManager.insertCategory(name, 0);
    }

    /**
     * Permanently deletes a category from the database.
     *
     * @param id database ID of the category to delete
     */
    public void deleteCategory(int id) {
        expenseManager.deleteCategory(id);
    }

    // -------------------------------------------------------------------------
    // Limit calculation
    // -------------------------------------------------------------------------

    /**
     * Recalculates and returns the recommended daily spending limit for today,
     * rounded up to the nearest EGP.
     *
     * @return daily limit in EGP (ceiling-rounded)
     */
    public double getDailyLimit() {
        BudgetCycle cycle = cycleManager.getActiveCycle();
        return Math.ceil(limitCalculator.recalculate(cycle.getId()));
    }

    /**
     * Returns the {@link LimitCalculator} instance for direct use by screens.
     *
     * @return the shared limit calculator
     */
    public LimitCalculator getLimitCalculator() {
        return limitCalculator;
    }

    // -------------------------------------------------------------------------
    // Auth delegation
    // -------------------------------------------------------------------------

    /**
     * Returns the {@link AuthManager} instance for PIN and lockout operations.
     *
     * @return the shared auth manager
     */
    public AuthManager getAuthManager() {
        return authManager;
    }

    // -------------------------------------------------------------------------
    // Navigation
    // -------------------------------------------------------------------------

    /**
     * Replaces the frame content with the {@link SetupScreen}.
     */
    public void showSetupScreen() {
        frame.setContentPane(new SetupScreen(this).getPanel());
        frame.revalidate();
    }

    /**
     * Replaces the frame content with the {@link DashboardScreen}.
     */
    public void showDashboardScreen() {
        frame.setContentPane(new DashboardScreen(this).getPanel());
        frame.revalidate();
    }

    /**
     * Replaces the frame content with the {@link ExpenseLoggingScreen}.
     */
    public void showExpenseLoggingScreen() {
        frame.setContentPane(new ExpenseLoggingScreen(this).getPanel());
        frame.revalidate();
    }

    /**
     * Replaces the frame content with the {@link HistoryScreen}.
     */
    public void showHistoryScreen() {
        frame.setContentPane(new HistoryScreen(this).getPanel());
        frame.revalidate();
    }

    /**
     * Replaces the frame content with the {@link SettingsScreen}.
     */
    public void showSettingsScreen() {
        frame.setContentPane(new SettingsScreen(this).getPanel());
        frame.revalidate();
    }

    /**
     * Replaces the frame content with the {@link LockScreen}.
     */
    public void showLockScreen() {
        frame.setContentPane(new LockScreen(this).getPanel());
        frame.revalidate();
    }
}
