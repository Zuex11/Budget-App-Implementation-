package ui;

import javax.swing.*;
import app.App;
import control.CycleManager;
import util.AppColors;
import util.UIFactory;

import java.awt.*;
import java.time.LocalDate;

public class SetupScreen extends BaseScreen{
    private JTextField allowanceField;
    private JSpinner startDay, startMonth, startYear;
    private JSpinner endDay, endMonth, endYear;
    private JButton startCycleButton;
    private JButton cancelButton;
    private JLabel errorLabel;

    public SetupScreen(App app) {
        super(app);
    }

    @Override
    protected void initComponents() {
        allowanceField = UIFactory.createTextField("e.g. 5,000.00");

        startDay   = UIFactory.createNumberSpinner(1, 1, 31);
        startMonth = UIFactory.createNumberSpinner(1, 1, 12);
        startYear  = UIFactory.createNumberSpinner(2026, 2024, 2100);
        endDay     = UIFactory.createNumberSpinner(1, 1, 31);
        endMonth   = UIFactory.createNumberSpinner(1, 1, 12);
        endYear    = UIFactory.createNumberSpinner(2026, 2024, 2100);

        startCycleButton = UIFactory.createPrimaryButton("Start cycle");
        cancelButton     = UIFactory.createSecondaryButton("Cancel");
        errorLabel       = UIFactory.createErrorLabel();

        startCycleButton.addActionListener(e -> onStartCycleClicked());
        cancelButton.addActionListener(e -> System.exit(0));
    }

    @Override
    protected void initLayout(){
        panel = new JPanel(new GridBagLayout());
        panel.setBackground(AppColors.BACKGROUND);

        JPanel card = UIFactory.createCard(520, 460);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;

        // Title
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        gbc.insets = new Insets(25, 20, 0, 20);
        card.add(UIFactory.createTitleLabel("Set up your budget cycle"), gbc);

        // Description
        gbc.gridy = 1;
        gbc.insets = new Insets(4, 20, 10, 20);
        card.add(UIFactory.createSubLabel("Define your allowance and the period you want to track spending for."), gbc);

        // Allowance label
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 20, 4, 20);
        card.add(UIFactory.createFieldLabel("Total allowance (EGP)"), gbc);

        // Allowance field
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 20, 10, 20);
        card.add(allowanceField, gbc);

        // Date labels
        gbc.gridy = 4; gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        gbc.gridx = 0; gbc.insets = new Insets(5, 20, 4, 10);
        card.add(UIFactory.createFieldLabel("Cycle start date"), gbc);

        gbc.gridx = 1; gbc.insets = new Insets(5, 10, 4, 20);
        card.add(UIFactory.createFieldLabel("Cycle end date"), gbc);

        // Start date spinners
        GridBagConstraints startGbc = new GridBagConstraints();
        startGbc.gridx = 0; startGbc.gridy = 5; startGbc.gridwidth = 1;
        startGbc.fill = GridBagConstraints.HORIZONTAL; startGbc.weightx = 1.0;
        startGbc.insets = new Insets(0, 20, 10, 10);
        card.add(buildDatePanel(startDay, startMonth, startYear), startGbc);

        // End date spinners
        GridBagConstraints endGbc = new GridBagConstraints();
        endGbc.gridx = 1; endGbc.gridy = 5; endGbc.gridwidth = 1;
        endGbc.fill = GridBagConstraints.HORIZONTAL; endGbc.weightx = 1.0;
        endGbc.insets = new Insets(0, 10, 10, 20);
        card.add(buildDatePanel(endDay, endMonth, endYear), endGbc);

        // Error label
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        gbc.insets = new Insets(0, 20, 5, 20);
        card.add(errorLabel, gbc);

        // Cancel button
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 20, 5, 20);
        card.add(cancelButton, gbc);

        // Start cycle button
        gbc.gridy = 8;
        gbc.insets = new Insets(0, 20, 25, 20);
        card.add(startCycleButton, gbc);

        // Add card centered in panel
        GridBagConstraints cardGbc = new GridBagConstraints();
        cardGbc.anchor = GridBagConstraints.CENTER;
        panel.add(card, cardGbc);
    }

    private JPanel buildDatePanel(JSpinner day, JSpinner month, JSpinner year) {
        JPanel inner = new JPanel(new GridBagLayout());
        inner.setBackground(AppColors.FIELD);

        JLabel slash1 = UIFactory.createSubLabel("/");
        slash1.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel slash2 = UIFactory.createSubLabel("/");
        slash2.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weighty = 1.0;

        g.gridx = 0; g.weightx = 0.3;  inner.add(day, g);
        g.gridx = 1; g.weightx = 0.05; inner.add(slash1, g);
        g.gridx = 2; g.weightx = 0.3;  inner.add(month, g);
        g.gridx = 3; g.weightx = 0.05; inner.add(slash2, g);
        g.gridx = 4; g.weightx = 0.4;  inner.add(year, g);

        return inner;
    }

    public void onStartCycleClicked() {
        if(validateInputs())
        {
            double allowance = Double.parseDouble(allowanceField.getText().replaceAll(",", ""));
            LocalDate startDate = LocalDate.of(
                    (int) startYear.getValue(),
                    (int) startMonth.getValue(),
                    (int) startDay.getValue()
            );
            LocalDate endDate = LocalDate.of(
                    (int) endYear.getValue(),
                    (int) endMonth.getValue(),
                    (int) endDay.getValue()
            );
            app.getCycleManager().initializeCycle(allowance, startDate, endDate);
        }
    }

    public boolean validateInputs() {
        String allowance = allowanceField.getText().trim();
        LocalDate todayDate = LocalDate.now();
        LocalDate startDate = LocalDate.of(
                (int) startYear.getValue(),
                (int) startMonth.getValue(),
                (int) startDay.getValue()
        );
        LocalDate endDate = LocalDate.of(
                (int) endYear.getValue(),
                (int) endMonth.getValue(),
                (int) endDay.getValue()
        );
        if (allowance.isEmpty()) {
            showError("Please fill all the fields!");
            return false;
        }
        try {
            double value = Double.parseDouble(allowance.replace(",", ""));
            if (value <= 0) {
                showError("Allowance must be greater than zero.");
                return false;
            }
        } catch (NumberFormatException ex) {
            showError("Allowance must be a valid number.");
            return false;
        }
        if(startDate.isBefore(todayDate))
        {
            showError("Start date must be after today.");
            return false;
        }
        if(endDate.isBefore(startDate))
        {
            showError("End date must be after start date.");
            return false;
        }

        errorLabel.setVisible(false);
        return true;
    }

    public void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    public void navigateToDashboard() {
        app.showDashboardScreen();
    }
}