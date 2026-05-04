package ui;

import app.App;
import javax.swing.*;
import java.awt.*;
import domain.Category;
import util.UIFactory;
import util.AppColors;

import java.time.LocalDate;
import java.util.List;

public class ExpenseLoggingScreen extends BaseScreen {
    private JTextField amountField;
    private JComboBox<Category> categoryComboBox;
    private JButton saveButton;
    private JButton cancelButton;
    private JLabel errorLabel;
    private JTextField newCategoryField;
    private JButton addCategoryButton;
    private LocalDate todayDate;
    private JLabel todayDateLabel;
    private JTextField todayDateField;

    private Category selectedCategory;

    public ExpenseLoggingScreen(App app) {
        super(app);
    }

    private void loadCategories() {
        categoryComboBox.removeAllItems();
        List<Category> categories = app.getCategories();
        for (Category c : categories) {
            categoryComboBox.addItem(c);
        }
        selectedCategory = (Category) categoryComboBox.getSelectedItem();
    }

    @Override
    protected void initComponents() {
        categoryComboBox = new JComboBox<>();

        categoryComboBox.addActionListener(e -> {
            selectedCategory = (Category) categoryComboBox.getSelectedItem();
        });

        amountField = UIFactory.createTextField("e.g.");
        saveButton = UIFactory.createPrimaryButton("Save");
        cancelButton = UIFactory.createSecondaryButton("Cancel");
        errorLabel = UIFactory.createErrorLabel();

        saveButton.addActionListener(e -> onSaveClicked());
        cancelButton.addActionListener(e -> app.showDashboardScreen());

        newCategoryField = UIFactory.createTextField("New category name");
        addCategoryButton = UIFactory.createSecondaryButton("Add");
        addCategoryButton.addActionListener(e -> {
            String name = newCategoryField.getText().trim();
            if (!name.isEmpty() && !name.equals("New category name")) {
                app.insertCategory(name);
                loadCategories();
                newCategoryField.setText("");
            }
        });
        todayDate = LocalDate.now();
        todayDateLabel = UIFactory.createSubLabel("Transaction date", 13);
        todayDateField = UIFactory.createTextField(todayDate.toString());
        todayDateField.setText(todayDate.toString());

        loadCategories();
    }

    @Override
    protected void initLayout() {
        panel = new JPanel(new BorderLayout());
        panel.setBackground(AppColors.BACKGROUND);
        panel.add(createNavBar("Log Expense"), BorderLayout.WEST);

        JPanel card = UIFactory.createCard(420, 430);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.insets = new Insets(25, 20, 0, 20);
        card.add(UIFactory.createTitleLabel("Log Expense", 15), gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(4, 20, 10, 20);

        card.add(UIFactory.createSubLabel("Enter the amount and select a category.", 13), gbc);
        JPanel centerPanel = new JPanel(new GridBagLayout());

        centerPanel.add(card, new GridBagConstraints());
        panel.add(centerPanel, BorderLayout.CENTER);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.insets = new Insets(5, 20, 4, 20);
        card.add(UIFactory.createFieldLabel("Amount (EGP)"), gbc);

        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 20, 10, 20);
        card.add(amountField, gbc);

        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 20, 10, 20);
        card.add(todayDateLabel, gbc);

        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 20, 10, 20);
        card.add(todayDateField, gbc);

        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.insets = new Insets(5, 20, 4, 20);
        card.add(UIFactory.createFieldLabel("Category"), gbc);

        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 20, 10, 20);
        card.add(categoryComboBox, gbc);

        gbc.gridy = 8;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.7;
        gbc.insets = new Insets(0, 20, 10, 5);
        card.add(newCategoryField, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.2;
        gbc.insets = new Insets(0, 0, 10, 20);
        card.add(addCategoryButton, gbc);

        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.insets = new Insets(0, 20, 5, 20);
        card.add(errorLabel, gbc);

        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.gridy = 9;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 20, 5, 20);
        card.add(cancelButton, gbc);

        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.gridy = 10;
        gbc.insets = new Insets(0, 20, 25, 20);
        card.add(saveButton, gbc);

    }

    public boolean validateInputs() {
        String amountText = amountField.getText().trim();
        if (amountText.isEmpty()) {
            showError("Amount is required.");
            return false;
        }
        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                showError("Amount must be greater than zero.");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Invalid amount format.");
            return false;
        }
        if (categoryComboBox.getSelectedItem() == null) {
            showError("Please select a category.");
            return false;
        }
        errorLabel.setVisible(false);
        return true;
    }

    public void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    public void onCategorySelected(Category category) {
        selectedCategory = category;
    }

    public void showSuccessAndReturn() {
        app.showDashboardScreen();
    }

    public void onSaveClicked() {
        if (validateInputs()) {
            double amount = Double.parseDouble(amountField.getText().trim());
            int categoryId = selectedCategory.getId();
            int cycleId = app.getActiveCycle().getId();
            app.logExpense(amount, categoryId);
            showSuccessAndReturn();
        }
    }
}