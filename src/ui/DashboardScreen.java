package ui;

import app.App;
import ui.BaseScreen;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartPanel;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.ChartFactory;
import util.UIFactory;


import javax.swing.*;
import java.awt.*;

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
    protected void initComponents()
    {
        dailyLimitLabel = UIFactory.createSubLabel("Daily limit:");
        totalSpentLabel = UIFactory.createSubLabel("Total spent:");
        remainingBalanceLabel = UIFactory.createSubLabel("Remaining balance:");
        budgetStatusLabel = UIFactory.createSubLabel("Budget status:");
        logExpense = UIFactory.createPrimaryButton("Log expense");

        //spendingPieChart = new JFreeChart();
    }
    @Override
    protected void initLayout()
    {
        panel = new JPanel(new BorderLayout());
        panel.add(createNavBar("DashBoard"), BorderLayout.WEST);
    }



}