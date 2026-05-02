package ui;

import app.App;
import ui.BaseScreen;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartPanel;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.ChartFactory;
import util.GaugePanel;
import util.UIFactory;


import javax.swing.*;
import java.awt.*;

public class DashboardScreen extends BaseScreen {
    private JLabel dailyLimitLabel;
    private JLabel totalSpentLabel;
    private JLabel remainingBalanceLabel;
    private JFreeChart spendingPieChart;
    private JButton logExpense;
    private JLabel budgetStatusLabel;
    private int titleFontSize = 25;
    private int subTitleFontSize = 18;



    public DashboardScreen(App app) {
    super(app);
    }
    @Override
    protected void initComponents()
    {
        dailyLimitLabel = UIFactory.createSubLabel("Daily limit:", subTitleFontSize);
        totalSpentLabel = UIFactory.createSubLabel("Total spent:", subTitleFontSize);
        remainingBalanceLabel = UIFactory.createSubLabel("Remaining balance:", subTitleFontSize);
        budgetStatusLabel = UIFactory.createSubLabel("Budget status:", subTitleFontSize);
        logExpense = UIFactory.createPrimaryButton("Log expense");

        //spendingPieChart = new JFreeChart();
    }
    @Override
    protected void initLayout()
    {
        panel = new JPanel(new BorderLayout());
        panel.add(createNavBar("DashBoard"), BorderLayout.WEST);
        GridBagConstraints gbc = new GridBagConstraints();
        GaugePanel gauge = new GaugePanel(0.65, "93", "EGP");

        panel.add(gauge);

    }

    
}