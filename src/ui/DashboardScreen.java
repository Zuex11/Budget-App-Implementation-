package ui;

import app.App;
import domain.BudgetCycle;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.ui.RectangleEdge;
import ui.BaseScreen;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartPanel;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.ChartFactory;
import util.AppColors;
import util.GaugePanel;
import util.UIFactory;


import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class DashboardScreen extends BaseScreen {
    private JLabel dailyLimitLabel;
    private JLabel totalSpentLabel;
    private JLabel remainingBalanceLabel;
    private double spentPercentage;
    private JFreeChart spendingPieChart;
    private JButton logExpense;
    private JLabel budgetStatusLabel;
    private int titleFontSize = 25;
    private int subTitleFontSize = 18;
    private GaugePanel gauge;
    private DefaultPieDataset budgetPieDataset;


    public DashboardScreen(App app) {
        super(app);
    }

    @Override
    protected void initComponents() {

        dailyLimitLabel = UIFactory.createSubLabel("Daily limit:", subTitleFontSize);
        totalSpentLabel = UIFactory.createSubLabel("Total spent:", subTitleFontSize);
        remainingBalanceLabel = UIFactory.createSubLabel("Remaining balance:", subTitleFontSize);
        budgetStatusLabel = UIFactory.createSubLabel("Budget status:", subTitleFontSize);
        logExpense = UIFactory.createPrimaryButton("Log expense");

        BudgetCycle cycle = app.getActiveCycle();
        if (cycle != null) {
            spentPercentage = cycle.getSpentPercentage();
        } else {
            spentPercentage = 0;
        }
        gauge = new GaugePanel(spentPercentage, dailyLimitLabel.getText(), "EGP");

        budgetPieDataset = new DefaultPieDataset();
        budgetPieDataset.setValue("Food", 45);
        budgetPieDataset.setValue("Transportation", 30);
        budgetPieDataset.setValue("Entertainment", 25);

        spendingPieChart = ChartFactory.createPieChart(
                null,
                budgetPieDataset,
                true,   // legend ON
                false,
                false
        );
    }

    @Override
    protected void initLayout() {
        panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 30));

        JPanel mainContent = new JPanel(new GridLayout(1, 2));
        mainContent.setBackground(new Color(30, 30, 30));

        JPanel leftSection = new JPanel(new GridBagLayout());
        leftSection.setBackground(new Color(30, 30, 30));
        leftSection.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(60, 60, 60)));

        JPanel rightSection = new JPanel(new GridBagLayout());
        rightSection.setBackground(new Color(30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);
        leftSection.add(createPieChart(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 10, 0);
        rightSection.add(gauge, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 20, 0);
        rightSection.add(logExpense, gbc);

        mainContent.add(leftSection);
        mainContent.add(rightSection);

        panel.add(createNavBar("DashBoard"), BorderLayout.WEST);
        panel.add(mainContent, BorderLayout.CENTER);
    }

    private ChartPanel createPieChart() {
        spendingPieChart = ChartFactory.createPieChart(null, budgetPieDataset, true, false, false);

        spendingPieChart.setBackgroundPaint(new Color(30, 30, 30));
        spendingPieChart.getLegend().setBackgroundPaint(new Color(30, 30, 30));
        spendingPieChart.getLegend().setItemPaint(Color.WHITE);
        spendingPieChart.getLegend().setFrame(new org.jfree.chart.block.BlockBorder(new Color(30, 30, 30)));
        spendingPieChart.getLegend().setPosition(RectangleEdge.BOTTOM);

        PiePlot plot = (PiePlot) spendingPieChart.getPlot();
        plot.setBackgroundPaint(new Color(30, 30, 30));
        plot.setOutlineVisible(false);
        plot.setShadowPaint(null);
        plot.setSectionOutlinesVisible(false);
        plot.setInteriorGap(0.05);
        plot.setCircular(true);
        plot.setSectionPaint("Food", new Color(155, 191, 224));
        plot.setSectionPaint("Transportation", new Color(155, 191, 224));
        plot.setSectionPaint("Entertainment", new Color(232, 160, 154));
        plot.setLabelGenerator(null);

        ChartPanel chartPanel = new ChartPanel(spendingPieChart);
        chartPanel.setBackground(new Color(30, 30, 30));
        chartPanel.setPreferredSize(new Dimension(300, 300));
        chartPanel.setMinimumSize(new Dimension(300, 300));
        chartPanel.setMaximumSize(new Dimension(300, 300));
        chartPanel.setMinimumDrawWidth(300);
        chartPanel.setMinimumDrawHeight(300);
        chartPanel.setMaximumDrawWidth(800);
        chartPanel.setMaximumDrawHeight(800);

        return chartPanel;
    }

    public void loadDashboardData() {
        BudgetCycle cycle = app.getActiveCycle();
        if (cycle == null) return;
        gauge.update(cycle.getSpentPercentage(), String.valueOf(app.getDailyLimit()));
        dailyLimitLabel.setText("Daily limit: " + app.getDailyLimit());
        remainingBalanceLabel.setText("Remaining: " + cycle.getRemainingBalance());
    }

    public void updateDailyLimit(double limit) {
        dailyLimitLabel.setText("Daily limit: " + limit);
    }

    public void showBudgetWarning(String message) {
        JOptionPane.showMessageDialog(panel, message, "Budget Warning", JOptionPane.WARNING_MESSAGE);
    }

    public void navigateToExpenseLogging() {
        app.showExpenseLoggingScreen();
    }
}