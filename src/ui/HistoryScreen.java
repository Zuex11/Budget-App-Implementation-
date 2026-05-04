package ui;

import app.App;
import domain.Category;
import util.AmountRenderer;
import util.CategoryPillRenderer;
import util.DefaultDarkRenderer;
import util.UIFactory;

import javax.sql.RowSet;
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


public class HistoryScreen  extends BaseScreen
{
    JTable transactionTable;
    DefaultTableModel transactionTableModel;
    JComboBox filterCategoryComboBox;
    JSpinner filterDateField;
    JButton clearFilterButton;
    JButton editButton;
    JButton deleteButton;
    JLabel titleLabel;
    JLabel subTitleLabel;
    JPanel filterPanel;
    List<JToggleButton> filterButtons;
    private final int  titleFontSize = 25;
    private final int subTitleFontSize = 18;

    public HistoryScreen(App app)
    {
        super(app);
    }

    @Override
    protected void initComponents()
    {
        titleLabel = UIFactory.createTitleLabel("Expense", titleFontSize);
        String thisMonthAndYear = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) +" "+ Integer.toString(LocalDate.now().getYear());
        subTitleLabel = UIFactory.createSubLabel("All transactions for " + thisMonthAndYear, subTitleFontSize);
        transactionTableModel = initTransactionTableModel();
        transactionTable = new JTable(transactionTableModel);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(transactionTableModel);
        transactionTable.setRowSorter(sorter);
        filterButtons = new ArrayList<>();

        filterPanel = UIFactory.createSubPanel(new GridBagLayout());

        initFilters();
        loadTransactions();
    }

    @Override
    protected void initLayout()
    {
        panel = UIFactory.createMainPanel(new  BorderLayout());
        JPanel mainContent = UIFactory.createMainPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1.0; gbc.weighty = 1.0; gbc.weighty= 0;
        gbc.insets = new Insets(4, 4, 0, 0);
        mainContent.add(titleLabel, gbc);

        gbc.gridy = 1; gbc.insets = new Insets(4, 4, 0, 0);
        mainContent.add(subTitleLabel, gbc);



        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        mainContent.add(filterPanel, gbc);

        gbc.gridy = 3;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 20, 20, 20);
        JScrollPane scrollPane = styleTransactionTable(transactionTable); // call it here
        mainContent.add(scrollPane, gbc);

//        mainContent.add(new JPanel() {{ setOpaque(false); }}, gbc);




        panel.add(mainContent,  BorderLayout.CENTER);
        panel.add(createNavBar("History"), BorderLayout.WEST);
    }
    private DefaultTableModel initTransactionTableModel()
    {
        transactionTableModel =  new DefaultTableModel(
            new String[]{"Description", "Category", "Date", "Amount"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
        return transactionTableModel;
    }
    private JScrollPane styleTransactionTable(JTable transactionTable)
    {
        List<Category> categories = app.getCategories();
        transactionTable.getColumnModel().getColumn(0).setCellRenderer(new DefaultDarkRenderer());
        transactionTable.getColumnModel().getColumn(1).setCellRenderer(new CategoryPillRenderer(categories));
        transactionTable.getColumnModel().getColumn(2).setCellRenderer(new DefaultDarkRenderer());
        transactionTable.getColumnModel().getColumn(3).setCellRenderer(new AmountRenderer());

        transactionTable.setBackground(new Color(42, 42, 42));
        transactionTable.setForeground(Color.WHITE);
        transactionTable.setSelectionBackground(new Color(83, 74, 183));
        transactionTable.setRowHeight(48);
        transactionTable.setShowGrid(false);
        transactionTable.setShowGrid(false);
        transactionTable.setIntercellSpacing(new Dimension(0, 0));
        transactionTable.setFont(new Font("Arial", Font.PLAIN, 13));

        JTableHeader header = transactionTable.getTableHeader();
        header.setBackground(new Color(30, 30, 30));
        header.setForeground(new Color(170, 170, 170));
        header.setFont(new Font("Arial", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(58, 58, 58)));
        scrollPane.getViewport().setBackground(new Color(42, 42, 42));
        return scrollPane;

    }

    public void loadTransactions()
    {
        transactionTableModel.setRowCount(0); // clear existing rows
        List<domain.Expense> expenses = app.getExpenses();
        List<Category> categories = app.getCategories();

        for (domain.Expense expense : expenses) {
            // find category name by id
            String categoryName = categories.stream()
                    .filter(c -> c.getId() == expense.getCategoryId())
                    .map(Category::getName)
                    .findFirst()
                    .orElse("Unknown");

            transactionTableModel.addRow(new Object[]{
                    categoryName,           // Description
                    categoryName,           // Category
                    expense.getTimestamp(), // Date
                    expense.getAmount() + " EGP" // Amount
            });
        }
    }
    public void initFilters()
    {
        filterPanel.removeAll();
        filterButtons.clear();

        // All button
        JToggleButton allBtn = new JToggleButton("All");
        allBtn.setSelected(true);
        allBtn.addActionListener(e -> {
            if(allBtn.isSelected()) {
                for(JToggleButton b : filterButtons) {
                    if(!b.getText().equals("All") && !b.getText().equals("None")) {
                        b.setSelected(false);
                    }
                }
                applyFilter();
            }
        });
        filterPanel.add(allBtn);
        filterButtons.add(allBtn);

        // None button
        JToggleButton noneBtn = new JToggleButton("None");
        noneBtn.addActionListener(e -> {
            if(noneBtn.isSelected()) {
                for(JToggleButton b : filterButtons) {
                    b.setSelected(false);
                }
                noneBtn.setSelected(true);
                applyFilter();
            }
        });
        filterPanel.add(noneBtn);
        filterButtons.add(noneBtn);

        // Category buttons
        for(Category cat : app.getCategories()) {
            JToggleButton btn = new JToggleButton(cat.getName());
            btn.addActionListener(e -> {
                if (btn.isSelected()) {
                    for(JToggleButton b : filterButtons) {
                        if(b.getText().equals("All") || b.getText().equals("None")) {
                            b.setSelected(false);
                        }
                    }
                }
                applyFilter();
            });
            filterButtons.add(btn);
            filterPanel.add(btn);
        }
        filterPanel.revalidate();
        filterPanel.repaint();
    }

    public void applyFilter()
    {
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) transactionTable.getRowSorter();

        if(sorter == null) return;

        boolean noneSelected = false;
        for(JToggleButton b : filterButtons)
        {
            if(b.isSelected() && b.getText().equals("None"))
            {
                noneSelected = true;
                break;
            }
        }

        if(noneSelected) {
            sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
                @Override
                public boolean include(RowFilter.Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                    return false;
                }
            });
            return;
        }

        boolean allSelected = false;
        for(JToggleButton b : filterButtons)
        {
            if(b.isSelected() && b.getText().equals("All"))
            {
                allSelected = true;
                break;
            }
        }

        if(allSelected) {
            sorter.setRowFilter(null);
        } else {
            boolean hasSelectedCategory = false;
            for(JToggleButton b : filterButtons) {
                if(b.isSelected() && !b.getText().equals("All") && !b.getText().equals("None")) {
                    hasSelectedCategory = true;
                    break;
                }
            }

            if(hasSelectedCategory) {
                sorter.setRowFilter(getRowFilter());
            } else {
                sorter.setRowFilter(null);
            }
        }
    }
    private RowFilter getRowFilter() {
        RowFilter<DefaultTableModel, Integer> filter = new RowFilter<DefaultTableModel, Integer>() {

            @Override
            public boolean include(RowFilter.Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                int categoryColumnIndex = 1;

                String categoryValue = entry.getStringValue(categoryColumnIndex);

                for(JToggleButton b : filterButtons) {
                    String buttonText = b.getText();
                    if(buttonText.equals("All") || buttonText.equals("None")) continue;

                    if(b.isSelected() && categoryValue.equals(buttonText)) {
                        return true;
                    }
                }
                return false;
            }
        };
        return filter;
    }
    public void clearFilter()
    {
        for(JToggleButton b : filterButtons) {
            if(!b.getText().equals("All") && !b.getText().equals("None")) {
                b.setSelected(false);
            }
        }
        for(JToggleButton b : filterButtons) {
            if(b.getText().equals("None")) {
                b.setSelected(false);
                break;
            }
        }
        for(JToggleButton b : filterButtons) {
            if(b.getText().equals("All")) {
                b.setSelected(true);
                break;
            }
        }

        applyFilter();
    }
    public void onEditClick()
    {}
    public void onDeleteClick()
    {}
    public void showEmptyState()
    {}
}