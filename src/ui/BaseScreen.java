package ui;

import app.App;
import util.UIFactory;

import javax.swing.*;
import java.awt.*;

public abstract class BaseScreen {
    protected JPanel panel;
    protected App app;

    public BaseScreen(App app) {
        this.app = app;
        initComponents();
        initLayout();
    }

    public JPanel createNavBar(String activeItem) {
        JPanel navBar = new JPanel();
        navBar.setLayout(new BoxLayout(navBar, BoxLayout.Y_AXIS));
        navBar.setBackground(new Color(42, 42, 42));
        navBar.setPreferredSize(new Dimension(180, 60));
        navBar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(58, 58, 58)));

        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new GridLayout(2, 1));
        logoPanel.setBackground(new Color(42, 42, 42));
        logoPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(58, 58, 58)));
        logoPanel.setMinimumSize(new Dimension(180, 60));
        logoPanel.setMaximumSize(new Dimension(180, 60));
        logoPanel.setPreferredSize(new Dimension(180, 60));

        JLabel appNameLabel = UIFactory.createNavTitleLabel("Mizan");
        JLabel cycleNameLabel = UIFactory.createNavSubLabel("May 2026", 15);

        appNameLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 0));
        cycleNameLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 0));

        logoPanel.add(appNameLabel);
        logoPanel.add(cycleNameLabel);

        navBar.add(logoPanel);

        navBar.add(createNavItem("DashBoard", activeItem.equals("DashBoard"), () -> app.showDashboardScreen()));
        navBar.add(createNavItem("Log Expense", activeItem.equals("Log Expense"), () -> app.showExpenseLoggingScreen()));
        navBar.add(createNavItem("History", activeItem.equals("History"), () -> app.showHistoryScreen()));
        navBar.add(createNavItem("Settings", activeItem.equals("Settings"), () -> app.showSettingsScreen()));

        return navBar;
    }

    private JPanel createNavItem(String text, boolean active, Runnable onClick) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setMaximumSize(new Dimension(180, 40));
        wrapper.setBackground(active ? new Color(55, 52, 90) : new Color(42, 42, 42));
        wrapper.setOpaque(true);
        if (active) {
            JPanel indicatorStrip = new JPanel();
            indicatorStrip.setBackground(new Color(127, 119, 221));
            indicatorStrip.setPreferredSize(new Dimension(2, 40));
            wrapper.add(indicatorStrip, BorderLayout.WEST);
        }

        JButton btn = createNavButton(text, active);
        btn.addActionListener(e -> onClick.run());
        wrapper.add(btn, BorderLayout.CENTER);

        return wrapper;
    }

    public JButton createNavButton(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(180, 40));
        btn.setPreferredSize(new Dimension(180, 40));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setBackground(active ? new Color(55, 52, 90) : new Color(42, 42, 42));
        return btn;
    }

    protected abstract void initComponents();
    protected abstract void initLayout();

    public JPanel getPanel() {
        return panel;
    }
}