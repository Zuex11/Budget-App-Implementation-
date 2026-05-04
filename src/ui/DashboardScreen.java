package ui;

import app.App;
import domain.BudgetCycle;
import domain.Category;
import domain.Expense;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartPanel;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.ChartFactory;
import util.GaugePanel;
import util.UIFactory;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DashboardScreen extends BaseScreen {
    private JLabel dailyLimitLabel;
    private JLabel totalSpentLabel;
    private JLabel remainingBalanceLabel;
    private double spentPercentage;
    private JFreeChart spendingPieChart;
    private JButton logExpenseBtn;
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
        logExpenseBtn = UIFactory.createPrimaryButton("Log expense");
        logExpenseBtn.addActionListener(e -> onLogExpenseClicked());

        BudgetCycle cycle = app.getActiveCycle();
        spentPercentage = (cycle != null) ? cycle.getSpentPercentage() / 100.0 : 0;
        gauge = new GaugePanel(spentPercentage, "0", "EGP");

        // Initialize dataset and chart BEFORE loadDashboardData
        budgetPieDataset = new DefaultPieDataset();
        spendingPieChart = ChartFactory.createPieChart(null, budgetPieDataset, true, false, false);

        loadDashboardData();
    }

    @Override
    protected void initLayout() {
        panel = UIFactory.createMainPanel(new BorderLayout());

        JPanel mainContent = new JPanel(new GridLayout(1, 2));
        mainContent.setBackground(new Color(30, 30, 30));

        JPanel leftSection = new JPanel(new GridBagLayout());
        leftSection.setBackground(new Color(30, 30, 30));
        leftSection.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(60, 60, 60)));

        JPanel rightSection = new JPanel(new GridBagLayout());
        rightSection.setBackground(new Color(30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);
        leftSection.add(createPieChart(), gbc);

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 10, 0);
        rightSection.add(gauge, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 20, 0);
        rightSection.add(logExpenseBtn, gbc);

        mainContent.add(leftSection);
        mainContent.add(rightSection);

        panel.add(createNavBar("DashBoard"), BorderLayout.WEST);
        panel.add(mainContent, BorderLayout.CENTER);
    }

    private ChartPanel createPieChart() {
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

    private void refreshPieChart() {
        budgetPieDataset.clear();
        BudgetCycle cycle = app.getActiveCycle();
        if (cycle == null) return;

        List<Category> categories = app.getCategories();
        Color[] palette = {
                new Color(155, 191, 224),
                new Color(232, 160, 154),
                new Color(144, 208, 144),
                new Color(255, 204, 128),
                new Color(186, 155, 224)
        };

        PiePlot plot = (PiePlot) spendingPieChart.getPlot();
        int i = 0;
        for (Category cat : categories) {
            List<Expense> expenses = app.getExpensesByCategory(cat.getId());
            double total = expenses.stream().mapToDouble(Expense::getAmount).sum();
            if (total > 0) {
                budgetPieDataset.setValue(cat.getName(), total);
                plot.setSectionPaint(cat.getName(), palette[i % palette.length]);
                i++;
            }
        }
    }
    public void loadDashboardData() {
        BudgetCycle cycle = app.getActiveCycle();
        if (cycle == null) return;

        double totalSpent = app.getLimitCalculator().calculateSpentTotal(cycle.getId());
        cycle.setTotalSpent(totalSpent);

        double dailyLimit = app.getDailyLimit();

        // Gauge fills based on today's spending vs daily limit
        double todaySpent = app.getTodaySpent(); // need this in App
        double percentage = (dailyLimit > 0) ? todaySpent / dailyLimit : 0;

        gauge.update(percentage, String.valueOf(dailyLimit));
        dailyLimitLabel.setText("Daily limit: " + dailyLimit);
        remainingBalanceLabel.setText("Remaining: " + cycle.getRemainingBalance());

        refreshPieChart();
    }

    private void onLogExpenseClicked() {
        app.showExpenseLoggingScreen();
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