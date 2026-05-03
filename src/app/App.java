package app;

import com.formdev.flatlaf.FlatDarkLaf;
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

public class App {
    private JFrame frame;
    private CycleManager cycleManager;
    private ExpenseManager expenseManager;
    private LimitCalculator limitCalculator;

    App() {
        cycleManager = new CycleManager();
        expenseManager = new ExpenseManager();
        limitCalculator = new LimitCalculator();

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

        if (cycleManager.getActiveCycle() == null) {
            showSetupScreen();
        } else {
            showDashboardScreen();
        }
    }

    public BudgetCycle getActiveCycle() {
        return cycleManager.getActiveCycle();
    }

    public BudgetCycle initializeCycle(double allowance, LocalDate start, LocalDate end) {
        return cycleManager.initializeCycle(allowance, start, end);
    }

    public Expense logExpense(double amount, int categoryId) {
        return expenseManager.logExpense(amount, categoryId, cycleManager.getActiveCycle().getId());
    }

    public List<Expense> getExpenses() {
        return expenseManager.getExpensesByCycle(cycleManager.getActiveCycle().getId());
    }

    public double getDailyLimit() {
        return limitCalculator.calculateDailyLimit(cycleManager.getActiveCycle());
    }

    public void resetCycle() {
        cycleManager.resetCycle();
    }

    public void showSetupScreen() {
        frame.setContentPane(new SetupScreen(this).getPanel());
        frame.revalidate();
    }

    public void showDashboardScreen() {
        frame.setContentPane(new DashboardScreen(this).getPanel());
        frame.revalidate();
    }

    public void showExpenseLoggingScreen() 
    {
        frame.setContentPane(new ExpenseLoggingScreen(this).getPanel());
        frame.revalidate();
    }

    public void showHistoryScreen() {
        frame.setContentPane(new HistoryScreen(this).getPanel());
        frame.revalidate();
    }

    public void showSettingsScreen() 
    {
        frame.setContentPane(new SettingsScreen(this).getPanel());
        frame.revalidate();
    }

    public List<Category> getCategories() 
    {
        return expenseManager.getCategories();
    }

    public Category insertCategory(String name) {
        return expenseManager.insertCategory(name, 0); // 0 for icon for now
    }
}