package ui;

import app.App;
import util.AmountRenderer;
import util.CategoryPillRenderer;
import util.DefaultDarkRenderer;
import util.UIFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.TextStyle;
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

    }

    @Override
    protected void initLayout()
    {
        panel = UIFactory.createMainPanel(new  BorderLayout());
        JPanel mainContent = UIFactory.createMainPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1.0; gbc.weighty = 1.0; gbc.weighty= 0;
        gbc.insets = new Insets(20, 20, 0, 0);
        mainContent.add(titleLabel, gbc);

        gbc.gridy = 1; gbc.insets = new Insets(4, 20, 0, 0);
        mainContent.add(subTitleLabel, gbc);




        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 20, 20, 20);
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
        transactionTableModel.addRow(new Object[]{"Groceries", "Food", "Apr 28, 2026", "350 EGP"});
        transactionTableModel.addRow(new Object[]{"Uber ride", "Transport", "Apr 27, 2026", "120 EGP"});
        transactionTableModel.addRow(new Object[]{"Pharmacy", "Health", "Apr 26, 2026", "75 EGP"});
        transactionTableModel.addRow(new Object[]{"Netflix", "Entertainment", "Apr 25, 2026", "180 EGP"});
        return transactionTableModel;
    }
    private JScrollPane styleTransactionTable(JTable transactionTable)
    {
        transactionTable.getColumnModel().getColumn(0).setCellRenderer(new DefaultDarkRenderer());
        transactionTable.getColumnModel().getColumn(1).setCellRenderer(new CategoryPillRenderer());
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

    }
    public void applyFilter()
    {}
    public void clearFilter()
    {}
    public void onEditClick()
    {}
    public void onDeleteClick()
    {}
    public void showEmptyState()
    {}
}