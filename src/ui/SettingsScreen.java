package ui;

import app.App;
import util.AppColors;
import util.UIFactory;

import javax.swing.*;
import java.awt.*;

public class SettingsScreen extends BaseScreen {
    private JButton resetCycleButton;
    private JCheckBox enablePinCheckBox;
    private JButton changePinButton;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JButton categoriesBtn, securityBtn, cycleBtn;

    public SettingsScreen(App app) { super(app); }

    @Override
    protected void initComponents() {
        resetCycleButton = UIFactory.createNormalButton("Reset");
        changePinButton = UIFactory.createNormalButton("Change PIN");
        enablePinCheckBox = new JCheckBox();
        enablePinCheckBox.setBackground(AppColors.CARD);

        cardLayout = new CardLayout();
        contentPanel = UIFactory.createSubPanel(cardLayout);
        contentPanel.add(buildCategoriesPanel(), "Categories");
        contentPanel.add(buildSecurityPanel(), "Security");
        contentPanel.add(buildCyclePanel(), "Cycle");

        resetCycleButton.addActionListener(e -> onResetCycleClicked());
        changePinButton.addActionListener(e -> onChangePinClicked());
        enablePinCheckBox.addActionListener(e -> onEnablePinToggled(enablePinCheckBox.isSelected()));
    }

    @Override
    protected void initLayout() {
        panel = UIFactory.createMainPanel(new BorderLayout());
        JPanel mainContent = UIFactory.createMainPanel(new BorderLayout());

        JPanel titlePanel = UIFactory.createMainPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1;
        gbc.insets = new Insets(20, 20, 0, 0);
        titlePanel.add(UIFactory.createTitleLabel("Settings", 22), gbc);
        gbc.gridy = 1; gbc.insets = new Insets(4, 20, 16, 0);
        titlePanel.add(UIFactory.createSubLabel("Manage your preferences and cycle configuration", 13), gbc);

        JPanel centerPanel = UIFactory.createMainPanel(new BorderLayout());
        centerPanel.add(createSubNavBar("Categories"), BorderLayout.WEST);
        centerPanel.add(contentPanel, BorderLayout.CENTER);

        mainContent.add(titlePanel, BorderLayout.NORTH);
        mainContent.add(centerPanel, BorderLayout.CENTER);

        panel.add(createNavBar("Settings"), BorderLayout.WEST);
        panel.add(mainContent, BorderLayout.CENTER);
    }

    public JPanel createSubNavBar(String activeItem) {
        JPanel navBar = new JPanel();
        navBar.setLayout(new BoxLayout(navBar, BoxLayout.Y_AXIS));
        navBar.setBackground(new Color(42, 42, 42));
        navBar.setPreferredSize(new Dimension(160, 600));
        navBar.setBorder(BorderFactory.createLineBorder(new Color(58, 58, 58)));

        categoriesBtn = buildSubNavBtn("Categories");
        securityBtn   = buildSubNavBtn("Security");
        cycleBtn      = buildSubNavBtn("Cycle");

        categoriesBtn.addActionListener(e -> { cardLayout.show(contentPanel, "Categories"); setActiveSubNav(categoriesBtn); });
        securityBtn.addActionListener(e   -> { cardLayout.show(contentPanel, "Security");   setActiveSubNav(securityBtn); });
        cycleBtn.addActionListener(e      -> { cardLayout.show(contentPanel, "Cycle");      setActiveSubNav(cycleBtn); });

        for (JButton btn : new JButton[]{categoriesBtn, securityBtn, cycleBtn}) {
            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setMaximumSize(new Dimension(160, 40));
            wrapper.setOpaque(true);
            wrapper.setBackground(btn.getBackground());
            wrapper.add(btn, BorderLayout.CENTER);
            navBar.add(wrapper);
        }

        setActiveSubNav(activeItem.equals("Security") ? securityBtn :
                activeItem.equals("Cycle")    ? cycleBtn    : categoriesBtn);

        navBar.add(Box.createVerticalGlue());
        return navBar;
    }

    private JButton buildSubNavBtn(String text) {
        JButton btn = new JButton(text);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBackground(new Color(42, 42, 42));
        btn.setForeground(new Color(136, 136, 136));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return btn;
    }

    private void setActiveSubNav(JButton active) {
        for (JButton btn : new JButton[]{categoriesBtn, securityBtn, cycleBtn}) {
            boolean isActive = btn == active;
            btn.setForeground(isActive ? new Color(127, 119, 221) : new Color(136, 136, 136));
            btn.setBackground(isActive ? new Color(55, 52, 90) : new Color(42, 42, 42));
            if (btn.getParent() != null)
                btn.getParent().setBackground(btn.getBackground());
        }
    }

    private JPanel buildSettingsRow(String title, String subtitle, JComponent action) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(AppColors.CARD);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(58, 58, 58)),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setBackground(AppColors.CARD);
        textPanel.add(UIFactory.createTitleLabel(title, 13));
        textPanel.add(UIFactory.createSubLabel(subtitle, 12));
        row.add(textPanel, BorderLayout.CENTER);
        row.add(action, BorderLayout.EAST);
        return row;
    }

    private JPanel buildCategoriesPanel() {
        JPanel p = UIFactory.createMainPanel(new BorderLayout());

        DefaultListModel<String> listModel = new DefaultListModel<>();
        app.getCategories().forEach(c -> listModel.addElement(c.getName()));

        JList<String> categoryList = new JList<>(listModel);
        categoryList.setBackground(AppColors.CARD);
        categoryList.setForeground(Color.WHITE);
        categoryList.setFont(new Font("SansSerif", Font.PLAIN, 13));
        categoryList.setSelectionBackground(new Color(83, 74, 183));
        categoryList.setFixedCellHeight(36);

        JScrollPane scroll = new JScrollPane(categoryList);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(58, 58, 58)));
        scroll.getViewport().setBackground(AppColors.CARD);

        JTextField newCatField = UIFactory.createTextField("New category name");
        JButton addBtn = UIFactory.createNormalButton("Add");
        JButton deleteBtn = UIFactory.createNormalButton("Delete");

        addBtn.addActionListener(e -> {
            String name = newCatField.getText().trim();
            if (!name.isEmpty() && !name.equals("New category name")) {
                app.insertCategory(name);
                listModel.addElement(name);
                newCatField.setText("");
            }
        });

        deleteBtn.addActionListener(e -> {
            int idx = categoryList.getSelectedIndex();
            if (idx != -1) {
                String name = listModel.getElementAt(idx);
                app.getCategories().stream()
                        .filter(c -> c.getName().equals(name))
                        .findFirst()
                        .ifPresent(c -> app.deleteCategory(c.getId()));
                listModel.remove(idx);
            }
        });

        JPanel bottomBar = UIFactory.createMainPanel(new BorderLayout());
        bottomBar.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JPanel btnPanel = UIFactory.createMainPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.add(deleteBtn);
        btnPanel.add(addBtn);
        bottomBar.add(newCatField, BorderLayout.CENTER);
        bottomBar.add(btnPanel, BorderLayout.EAST);

        p.add(scroll, BorderLayout.CENTER);
        p.add(bottomBar, BorderLayout.SOUTH);
        return p;
    }

    private JPanel buildSecurityPanel() {
        JPanel p = UIFactory.createMainPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1; gbc.gridx = 0; gbc.insets = new Insets(8, 16, 8, 16);

        JPanel changePinRow = buildSettingsRow("Change PIN", "Update your 4-digit security PIN", changePinButton);
        changePinRow.setVisible(app.getAuthManager().hasPin());

        enablePinCheckBox.setSelected(app.getAuthManager().hasPin());
        enablePinCheckBox.addActionListener(e -> {
            onEnablePinToggled(enablePinCheckBox.isSelected());
            changePinRow.setVisible(enablePinCheckBox.isSelected());
        });

        gbc.gridy = 0; p.add(buildSettingsRow("Enable PIN lock", "Require a PIN when opening the app", enablePinCheckBox), gbc);
        gbc.gridy = 1; p.add(changePinRow, gbc);
        gbc.gridy = 2; gbc.weighty = 1; gbc.fill = GridBagConstraints.BOTH;
        p.add(new JPanel() {{ setOpaque(false); }}, gbc);
        return p;
    }

    private JPanel buildCyclePanel() {
        JPanel p = UIFactory.createMainPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1; gbc.gridx = 0; gbc.insets = new Insets(8, 16, 8, 16);
        gbc.gridy = 0; p.add(buildSettingsRow("Reset cycle", "This will permanently delete all logs.", resetCycleButton), gbc);
        gbc.gridy = 1; gbc.weighty = 1; gbc.fill = GridBagConstraints.BOTH;
        p.add(new JPanel() {{ setOpaque(false); }}, gbc);
        return p;
    }

    public void onResetCycleClicked() {
        int confirm = JOptionPane.showConfirmDialog(panel,
                "This will permanently delete all logs. Continue?",
                "Reset Cycle", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            app.resetCycle();
            app.showSetupScreen();
        }
    }

    public void onEnablePinToggled(boolean isEnabled) {
        if (isEnabled) {
            String pin = JOptionPane.showInputDialog(panel, "Set a 4-digit PIN:");
            if (pin != null && pin.matches("\\d{4}")) {
                app.getAuthManager().setPIN(pin);
                changePinButton.setVisible(true);
            } else {
                enablePinCheckBox.setSelected(false);
                changePinButton.setVisible(false);
                JOptionPane.showMessageDialog(panel, "PIN must be exactly 4 digits.");
            }
        } else {
            app.getAuthManager().setPIN(null);
            changePinButton.setVisible(false);
        }
    }

    public void onChangePinClicked() {
        String pin = JOptionPane.showInputDialog(panel, "Enter new 4-digit PIN:");
        if (pin != null && pin.matches("\\d{4}")) {
            app.getAuthManager().setPIN(pin);
        } else {
            JOptionPane.showMessageDialog(panel, "PIN must be exactly 4 digits.");
        }
    }
}