package ui;

import app.App;
import javax.swing.*;
import java.awt.*;
import domain.Category;
import util.UIFactory;
import util.AppColors;

import java.time.LocalDate;
import java.util.List;

/**
 * Screen that allows the user to log a new expense.
 *
 * <p>Collects an amount in EGP and a category selection, validates the inputs,
 * then delegates to {@link App#logExpense} before returning to the dashboard.</p>
 */
public class ExpenseLoggingScreen extends BaseScreen {

    /** Input field for the expense amount in EGP. */
    private JTextField amountField;

    /** Drop-down for selecting the expense category. */
    private JComboBox<Category> categoryComboBox;

    /** Button that submits the expense. */
    private JButton saveButton;

    /** Button that cancels and returns to the dashboard. */
    private JButton cancelButton;

    /** Label used to surface validation error messages. */
    private JLabel errorLabel;

    /** Today's date, used to pre-fill the transaction date field. */
    private LocalDate todayDate;

    /** Label for the transaction date field. */
    private JLabel todayDateLabel;

    /** Read-only field showing today's date as the transaction date. */
    private JTextField todayDateField;

    /** Currently selected category, kept in sync with the combo box. */
    private Category selectedCategory;

    /**
     * Constructs the ExpenseLoggingScreen.
     *
     * @param app the central application controller
     */
    public ExpenseLoggingScreen(App app) {
        super(app);
    }

    /**
     * Populates the category combo box with all categories from the database
     * and syncs {@link #selectedCategory} to the first item.
     */
    private void loadCategories() {
        categoryComboBox.removeAllItems();
        for (Category c : app.getCategories()) categoryComboBox.addItem(c);
        selectedCategory = (Category) categoryComboBox.getSelectedItem();
    }

    /** {@inheritDoc} */
    @Override
    protected void initComponents() {
        categoryComboBox = new JComboBox<>();
        categoryComboBox.addActionListener(e -> selectedCategory = (Category) categoryComboBox.getSelectedItem());

        amountField  = UIFactory.createTextField("e.g. 50.00");
        saveButton   = UIFactory.createPrimaryButton("Save");
        cancelButton = UIFactory.createSecondaryButton("Cancel");
        errorLabel   = UIFactory.createErrorLabel();

        saveButton.addActionListener(e -> onSaveClicked());
        cancelButton.addActionListener(e -> app.showDashboardScreen());

        todayDate      = LocalDate.now();
        todayDateLabel = UIFactory.createSubLabel("Transaction date", 13);
        todayDateField = UIFactory.createTextField(todayDate.toString());
        todayDateField.setText(todayDate.toString());

        loadCategories();
    }

    /** {@inheritDoc} */
    @Override
    protected void initLayout() {
        panel = new JPanel(new BorderLayout());
        panel.setBackground(AppColors.BACKGROUND);
        panel.add(createNavBar("Log Expense"), BorderLayout.WEST);

        JPanel card = UIFactory.createCard(420, 400);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        gbc.insets = new Insets(25, 20, 0, 20);
        card.add(UIFactory.createTitleLabel("Log Expense", 15), gbc);

        gbc.gridy = 1; gbc.insets = new Insets(4, 20, 10, 20);
        card.add(UIFactory.createSubLabel("Enter the amount and select a category.", 13), gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        gbc.insets = new Insets(5, 20, 4, 20);
        card.add(UIFactory.createFieldLabel("Amount (EGP)"), gbc);

        gbc.gridy = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 20, 10, 20);
        card.add(amountField, gbc);

        gbc.gridy = 4; card.add(todayDateLabel, gbc);
        gbc.gridy = 5; card.add(todayDateField, gbc);

        gbc.gridy = 6; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        gbc.insets = new Insets(5, 20, 4, 20);
        card.add(UIFactory.createFieldLabel("Category"), gbc);

        gbc.gridy = 7; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 20, 10, 20);
        card.add(categoryComboBox, gbc);

        gbc.gridy = 8; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        gbc.insets = new Insets(0, 20, 5, 20);
        card.add(errorLabel, gbc);

        gbc.gridy = 9; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 20, 5, 20);
        card.add(cancelButton, gbc);

        gbc.gridy = 10; gbc.insets = new Insets(0, 20, 25, 20);
        card.add(saveButton, gbc);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(AppColors.BACKGROUND);
        centerPanel.add(card, new GridBagConstraints());
        panel.add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * Validates all form inputs before saving.
     * Shows an inline error and returns {@code false} on the first failure.
     *
     * @return {@code true} if amount is a positive number and a category is selected
     */
    public boolean validateInputs() {
        String amountText = amountField.getText().trim();
        if (amountText.isEmpty()) { showError("Amount is required."); return false; }
        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) { showError("Amount must be greater than zero."); return false; }
        } catch (NumberFormatException e) { showError("Invalid amount format."); return false; }
        if (categoryComboBox.getSelectedItem() == null) { showError("Please select a category."); return false; }
        errorLabel.setVisible(false);
        return true;
    }

    /**
     * Shows a validation error message below the form.
     *
     * @param message the error text to display
     */
    public void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    /**
     * Updates the selected category when the combo box selection changes.
     *
     * @param category the newly selected {@link Category}
     */
    public void onCategorySelected(Category category) {
        selectedCategory = category;
    }

    /**
     * Navigates back to the dashboard after a successful save.
     */
    public void showSuccessAndReturn() {
        app.showDashboardScreen();
    }

    /**
     * Handles the "Save" button click.
     * Validates inputs, logs the expense, recalculates the daily limit,
     * and returns to the dashboard.
     */
    public void onSaveClicked() {
        if (validateInputs()) {
            double amount = Double.parseDouble(amountField.getText().trim());
            app.logExpense(amount, selectedCategory.getId());
            app.getLimitCalculator().recalculate(app.getActiveCycle().getId());
            showSuccessAndReturn();
        }
    }
}
