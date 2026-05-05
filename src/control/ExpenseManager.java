package control;

import domain.*;
import persistence.DatabaseHelper;

import java.util.List;

public class ExpenseManager {
    private DatabaseHelper dbHelper;
    private AlertManager alertManager;
    private LimitCalculator limitCalculator;

    public ExpenseManager() {
        this.dbHelper = DatabaseHelper.getInstance();
        this.alertManager = new AlertManager();
        this.limitCalculator = new LimitCalculator();
    }

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

    public void editExpense(int id, double amount, int categoryId) {
        Expense expense = new Expense(amount, categoryId, 0);
        expense.setId(id);
        dbHelper.updateExpense(expense);
    }

    public void deleteExpense(int id) {
        dbHelper.deleteExpense(id);
    }

    public List<Expense> getExpensesByCycle(int cycleId) {
        return dbHelper.getAllExpenses(cycleId);
    }

    public List<Expense> getExpensesByCategory(int cycleId, int categoryId) {
        return dbHelper.getExpensesByCategory(cycleId, categoryId);
    }

    public Category insertCategory(String name, int iconResId) 
    {
        Category category = new Category(0, name, iconResId);
        long id = dbHelper.insertCategory(category);
        category = new Category((int) id, name, iconResId);
        return category;
    }

    public List<Category> getCategories() 
    {
        return dbHelper.getAllCategories();
    }
    public double getTodaySpent(int cycleId) {
        return dbHelper.getTodaySpent(cycleId);
    }
    public void deleteCategory(int id) {
        dbHelper.deleteCategory(id);
    }
}