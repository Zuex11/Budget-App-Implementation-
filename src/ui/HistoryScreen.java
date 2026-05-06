package ui;

import app.App;
import domain.Category;
import domain.Expense;
import util.AmountRenderer;
import util.CategoryPillRenderer;
import util.DefaultDarkRenderer;
import util.UIFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Screen that displays the full expense history for the current cycle.
 *
 * <p>Features a sortable table, category filter toggle buttons, and
 * inline edit/delete actions on selected rows.</p>
 */
public class HistoryScreen extends BaseScreen {

    /** Table showing all expense transactions. */
    JTable transactionTable;

    /** Backing model for the transaction table. */
    DefaultTableModel transactionTableModel;

    /** Title label at the top of the screen. */
    JLabel titleLabel;

    /** Subtitle showing the current month and year. */
    JLabel subTitleLabel;

    /** Panel holding the category filter toggle buttons. */
    JPanel filterPanel;

    /** All category filter toggle buttons including "All" and "None". */
    List<JToggleButton> filterButtons;

    /** Button to edit the selected expense row. */
    private JButton editButton;

    /** Button to delete the selected expense row. */
    private JButton deleteButton;

    /** In-memory list of expenses currently loaded into the table. */
    private List<Expense> loadedExpenses;

    /** Font size for the screen title label. */
    private final int titleFontSize = 25;
    /** Font size for the screen subtitle label. */
    private final int subTitleFontSize = 18;

    /**
     * Constructs the HistoryScreen.
     *
     * @param app the central application controller
     */
    public HistoryScreen(App app) {
        super(app);
    }

    /** {@inheritDoc} */
    @Override
    protected void initComponents() {
        titleLabel = UIFactory.createTitleLabel("Expense", titleFontSize);
        String thisMonthAndYear = LocalDate.now().getMonth()
            .getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + LocalDate.now().getYear();
        subTitleLabel = UIFactory.createSubLabel("All transactions for " + thisMonthAndYear, subTitleFontSize);

        transactionTableModel = initTransactionTableModel();
        transactionTable = new JTable(transactionTableModel);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(transactionTableModel);
        transactionTable.setRowSorter(sorter);

        filterButtons = new ArrayList<>();
        filterPanel   = UIFactory.createSubPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));

        editButton   = UIFactory.createSecondaryButton("Edit");
        deleteButton = UIFactory.createSecondaryButton("Delete");
        deleteButton.setForeground(new Color(220, 80, 80));

        editButton.addActionListener(e -> onEditClick());
        deleteButton.addActionListener(e -> onDeleteClick());

        loadedExpenses = new ArrayList<>();
        initFilters();
        loadTransactions();
    }

    /** {@inheritDoc} */
    @Override
    protected void initLayout() {
        panel = UIFactory.createMainPanel(new BorderLayout());
        JPanel mainContent = UIFactory.createMainPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1.0; gbc.weighty = 0;
        gbc.insets = new Insets(12, 12, 0, 0);
        mainContent.add(titleLabel, gbc);

        gbc.gridy = 1; gbc.insets = new Insets(4, 12, 0, 0);
        mainContent.add(subTitleLabel, gbc);

        gbc.gridy = 2; gbc.insets = new Insets(8, 8, 0, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainContent.add(filterPanel, gbc);

        JPanel actionPanel = UIFactory.createMainPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        editButton.setPreferredSize(new Dimension(80, 32));
        deleteButton.setPreferredSize(new Dimension(80, 32));
        actionPanel.add(editButton);
        actionPanel.add(deleteButton);
        gbc.gridy = 3; gbc.insets = new Insets(0, 8, 0, 8);
        mainContent.add(actionPanel, gbc);

        gbc.gridy = 4; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(4, 12, 12, 12);
        mainContent.add(styleTransactionTable(transactionTable), gbc);

        panel.add(mainContent, BorderLayout.CENTER);
        panel.add(createNavBar("History"), BorderLayout.WEST);
    }

    /**
     * Creates a non-editable table model with columns:
     * Description, Category, Date, Amount.
     *
     * @return the configured {@link DefaultTableModel}
     */
    private DefaultTableModel initTransactionTableModel() {
        return new DefaultTableModel(new String[]{"Description", "Category", "Date", "Amount"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    /**
     * Applies dark-themed cell renderers and styling to the transaction table,
     * then wraps it in a scroll pane.
     *
     * @param table the table to style
     * @return a styled {@link JScrollPane} containing the table
     */
    private JScrollPane styleTransactionTable(JTable table) {
        List<Category> categories = app.getCategories();
        table.getColumnModel().getColumn(0).setCellRenderer(new DefaultDarkRenderer());
        table.getColumnModel().getColumn(1).setCellRenderer(new CategoryPillRenderer(categories));
        table.getColumnModel().getColumn(2).setCellRenderer(new DefaultDarkRenderer());
        table.getColumnModel().getColumn(3).setCellRenderer(new AmountRenderer());

        table.setBackground(new Color(42, 42, 42));
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(83, 74, 183));
        table.setRowHeight(48);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFont(new Font("Arial", Font.PLAIN, 13));

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(30, 30, 30));
        header.setForeground(new Color(170, 170, 170));
        header.setFont(new Font("Arial", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(58, 58, 58)));
        scrollPane.getViewport().setBackground(new Color(42, 42, 42));
        return scrollPane;
    }

    /**
     * Clears and reloads the table from the database.
     * Also refreshes {@link #loadedExpenses} used for row-level operations.
     */
    public void loadTransactions() {
        transactionTableModel.setRowCount(0);
        loadedExpenses = app.getExpenses();
        List<Category> categories = app.getCategories();

        for (Expense expense : loadedExpenses) {
            String categoryName = categories.stream()
                .filter(c -> c.getId() == expense.getCategoryId())
                .map(Category::getName)
                .findFirst()
                .orElse("Unknown");

            transactionTableModel.addRow(new Object[]{
                categoryName,
                categoryName,
                expense.getTimestamp(),
                expense.getAmount() + " EGP"
            });
        }
    }

    /**
     * Builds and registers all category filter toggle buttons.
     * Clears any previously registered buttons before rebuilding.
     */
    public void initFilters() {
        filterPanel.removeAll();
        filterButtons.clear();

        JToggleButton allBtn = new JToggleButton("All");
        allBtn.setSelected(true);
        allBtn.addActionListener(e -> {
            if (allBtn.isSelected()) {
                for (JToggleButton b : filterButtons)
                    if (!b.getText().equals("All") && !b.getText().equals("None"))
                        b.setSelected(false);
                applyFilter();
            }
        });
        filterPanel.add(allBtn);
        filterButtons.add(allBtn);

        JToggleButton noneBtn = new JToggleButton("None");
        noneBtn.addActionListener(e -> {
            if (noneBtn.isSelected()) {
                for (JToggleButton b : filterButtons) b.setSelected(false);
                noneBtn.setSelected(true);
                applyFilter();
            }
        });
        filterPanel.add(noneBtn);
        filterButtons.add(noneBtn);

        for (Category cat : app.getCategories()) {
            JToggleButton btn = new JToggleButton(cat.getName());
            btn.addActionListener(e -> {
                if (btn.isSelected()) {
                    for (JToggleButton b : filterButtons)
                        if (b.getText().equals("All") || b.getText().equals("None"))
                            b.setSelected(false);
                }
                applyFilter();
            });
            filterButtons.add(btn);
            filterPanel.add(btn);
        }

        filterPanel.revalidate();
        filterPanel.repaint();
    }

    /**
     * Applies the active category filter to the table row sorter.
     * Shows all rows when "All" is selected; hides all rows when "None" is selected;
     * otherwise filters to the selected category names.
     */
    public void applyFilter() {
        TableRowSorter<DefaultTableModel> sorter =
            (TableRowSorter<DefaultTableModel>) transactionTable.getRowSorter();
        if (sorter == null) return;

        for (JToggleButton b : filterButtons) {
            if (b.isSelected() && b.getText().equals("None")) {
                sorter.setRowFilter(RowFilter.andFilter(List.of(RowFilter.regexFilter("^$", 0))));
                return;
            }
        }
        for (JToggleButton b : filterButtons) {
            if (b.isSelected() && b.getText().equals("All")) {
                sorter.setRowFilter(null);
                return;
            }
        }

        List<String> selected = new ArrayList<>();
        for (JToggleButton b : filterButtons)
            if (b.isSelected() && !b.getText().equals("All") && !b.getText().equals("None"))
                selected.add(b.getText());

        if (selected.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.orFilter(
                selected.stream()
                    .map(name -> RowFilter.<DefaultTableModel, Integer>regexFilter(
                        "^" + java.util.regex.Pattern.quote(name) + "$", 1))
                    .toList()
            ));
        }
    }

    /**
     * Resets all filters to the "All" state.
     */
    public void clearFilter() {
        for (JToggleButton b : filterButtons) b.setSelected(false);
        for (JToggleButton b : filterButtons) {
            if (b.getText().equals("All")) { b.setSelected(true); break; }
        }
        applyFilter();
    }

    /**
     * Handles the "Edit" button click.
     * Opens a dialog pre-filled with the selected expense's data and saves changes.
     */
    public void onEditClick() {
        int viewRow = transactionTable.getSelectedRow();
        if (viewRow < 0) { showEmptyState(); return; }

        int modelRow = transactionTable.convertRowIndexToModel(viewRow);
        Expense expense = loadedExpenses.get(modelRow);

        JTextField amountField = UIFactory.createTextField(String.valueOf(expense.getAmount()));
        amountField.setText(String.valueOf(expense.getAmount()));

        JComboBox<Category> categoryBox = new JComboBox<>();
        List<Category> categories = app.getCategories();
        for (Category c : categories) categoryBox.addItem(c);
        categories.stream()
            .filter(c -> c.getId() == expense.getCategoryId())
            .findFirst()
            .ifPresent(categoryBox::setSelectedItem);

        JPanel dialogPanel = new JPanel(new GridLayout(4, 1, 4, 4));
        dialogPanel.add(new JLabel("New amount (EGP):"));
        dialogPanel.add(amountField);
        dialogPanel.add(new JLabel("Category:"));
        dialogPanel.add(categoryBox);

        int result = JOptionPane.showConfirmDialog(panel, dialogPanel,
            "Edit Expense", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;

        try {
            double newAmount = Double.parseDouble(amountField.getText().trim());
            if (newAmount <= 0) throw new NumberFormatException();
            Category selectedCat = (Category) categoryBox.getSelectedItem();
            app.editExpense(expense.getId(), newAmount, selectedCat.getId());
            loadTransactions();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(panel, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Handles the "Delete" button click.
     * Shows a confirmation dialog and deletes the selected expense on confirmation.
     */
    public void onDeleteClick() {
        int viewRow = transactionTable.getSelectedRow();
        if (viewRow < 0) { showEmptyState(); return; }

        int modelRow = transactionTable.convertRowIndexToModel(viewRow);
        Expense expense = loadedExpenses.get(modelRow);

        int confirm = JOptionPane.showConfirmDialog(panel,
            "Delete this expense of " + expense.getAmount() + " EGP?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            app.deleteExpense(expense.getId());
            loadTransactions();
        }
    }

    /**
     * Shows an information dialog prompting the user to select a row first.
     */
    public void showEmptyState() {
        JOptionPane.showMessageDialog(panel, "Please select a transaction first.",
            "No Selection", JOptionPane.INFORMATION_MESSAGE);
    }
}
