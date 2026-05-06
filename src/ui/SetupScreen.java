package ui;

import javax.swing.*;
import app.App;
import util.AppColors;
import util.UIFactory;

import java.awt.*;
import java.time.LocalDate;

/**
 * First-run screen where the user defines their budget cycle.
 *
 * <p>Collects a total allowance and a start/end date range, validates the
 * inputs, then delegates to {@link App#initializeCycle} before navigating
 * to the dashboard.</p>
 */
public class SetupScreen extends BaseScreen {

    /** Input field for the total budget allowance in EGP. */
    private JTextField allowanceField;

    /** Day spinner for the cycle start date. */
    private JSpinner startDay;

    /** Month spinner for the cycle start date. */
    private JSpinner startMonth;

    /** Year spinner for the cycle start date. */
    private JSpinner startYear;

    /** Day spinner for the cycle end date. */
    private JSpinner endDay;

    /** Month spinner for the cycle end date. */
    private JSpinner endMonth;

    /** Year spinner for the cycle end date. */
    private JSpinner endYear;

    /** Button that submits the form and creates the cycle. */
    private JButton startCycleButton;

    /** Button that exits the application (no cycle = no app). */
    private JButton cancelButton;

    /** Label used to surface validation error messages. */
    private JLabel errorLabel;

    /** Font size for the screen title label. */
    private final int titleFontSize = 25;
    /** Font size for the screen subtitle label. */
    private final int subTitleFontSize = 18;

    /**
     * Constructs the SetupScreen.
     *
     * @param app the central application controller
     */
    public SetupScreen(App app) {
        super(app);
    }

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
    @Override
    protected void initLayout() {
        panel = new JPanel(new GridBagLayout());
        panel.setBackground(AppColors.BACKGROUND);

        JPanel card = UIFactory.createCard(520, 460);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        gbc.insets = new Insets(25, 20, 0, 20);
        card.add(UIFactory.createTitleLabel("Set up your budget cycle", titleFontSize), gbc);

        gbc.gridy = 1; gbc.insets = new Insets(4, 20, 10, 20);
        card.add(UIFactory.createSubLabel(
            "Define your allowance and the period you want to track spending for.", subTitleFontSize), gbc);

        gbc.gridy = 2; gbc.insets = new Insets(5, 20, 4, 20);
        card.add(UIFactory.createFieldLabel("Total allowance (EGP)"), gbc);

        gbc.gridy = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 20, 10, 20);
        card.add(allowanceField, gbc);

        gbc.gridy = 4; gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        gbc.gridx = 0; gbc.insets = new Insets(5, 20, 4, 10);
        card.add(UIFactory.createFieldLabel("Cycle start date"), gbc);

        gbc.gridx = 1; gbc.insets = new Insets(5, 10, 4, 20);
        card.add(UIFactory.createFieldLabel("Cycle end date"), gbc);

        GridBagConstraints startGbc = new GridBagConstraints();
        startGbc.gridx = 0; startGbc.gridy = 5; startGbc.gridwidth = 1;
        startGbc.fill = GridBagConstraints.HORIZONTAL; startGbc.weightx = 1.0;
        startGbc.insets = new Insets(0, 20, 10, 10);
        card.add(buildDatePanel(startDay, startMonth, startYear), startGbc);

        GridBagConstraints endGbc = new GridBagConstraints();
        endGbc.gridx = 1; endGbc.gridy = 5; endGbc.gridwidth = 1;
        endGbc.fill = GridBagConstraints.HORIZONTAL; endGbc.weightx = 1.0;
        endGbc.insets = new Insets(0, 10, 10, 20);
        card.add(buildDatePanel(endDay, endMonth, endYear), endGbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        gbc.insets = new Insets(0, 20, 5, 20);
        card.add(errorLabel, gbc);

        gbc.gridy = 7; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 20, 5, 20);
        card.add(cancelButton, gbc);

        gbc.gridy = 8; gbc.insets = new Insets(0, 20, 25, 20);
        card.add(startCycleButton, gbc);

        panel.add(card, new GridBagConstraints());
    }

    /**
     * Builds a compact inline date panel with day / month / year spinners
     * separated by slash labels.
     *
     * @param day   spinner for the day component (1–31)
     * @param month spinner for the month component (1–12)
     * @param year  spinner for the year component
     * @return the assembled date panel
     */
    private JPanel buildDatePanel(JSpinner day, JSpinner month, JSpinner year) {
        JPanel inner = new JPanel(new GridBagLayout());
        inner.setBackground(AppColors.FIELD);

        JLabel slash1 = UIFactory.createSubLabel("/", subTitleFontSize);
        slash1.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel slash2 = UIFactory.createSubLabel("/", subTitleFontSize);
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

    /**
     * Handles the "Start cycle" button click.
     * Validates inputs and, if valid, creates the cycle and navigates to the dashboard.
     */
    public void onStartCycleClicked() {
        if (validateInputs()) {
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
            app.initializeCycle(allowance, startDate, endDate);
            app.showDashboardScreen();
        }
    }

    /**
     * Validates all user inputs before cycle creation.
     * Displays an appropriate error message and returns {@code false} if any
     * check fails.
     *
     * @return {@code true} if all inputs are valid, {@code false} otherwise
     */
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
        if (startDate.isBefore(todayDate)) {
            showError("Start date must be after today.");
            return false;
        }
        if (endDate.isBefore(startDate)) {
            showError("End date must be after start date.");
            return false;
        }

        errorLabel.setVisible(false);
        return true;
    }

    /**
     * Displays a validation error message below the form.
     *
     * @param message the error text to show
     */
    public void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    /**
     * Navigates to the dashboard screen.
     */
    public void navigateToDashboard() {
        app.showDashboardScreen();
    }
}
