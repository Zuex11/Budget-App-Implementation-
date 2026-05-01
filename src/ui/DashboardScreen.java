package ui;

import app.App;
import ui.BaseScreen;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartPanel;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.ChartFactory;


import javax.swing.*;

public class DashboardScreen extends BaseScreen {
    JLabel dailyLimitLabel;
    JLabel totalSpentLabel;
    JLabel remainingBalanceLabel;
    JFreeChart spendingPieChart;
    JButton logExpense;
    JLabel budgetStatusLabel;

    public DashboardScreen(App app) {
    super(app);
    }
    @Override
    protected void initLayout() {
    }
    @Override
    protected void initComponents() {
    }


}