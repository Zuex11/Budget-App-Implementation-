package ui;

import app.App;
import util.UIFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
        transactionTableModel = new DefaultTableModel(
                new String[]{"Date", "Category", "Amount"}, 0
        );
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

        mainContent.add(transactionTable, gbc);

//        mainContent.add(new JPanel() {{ setOpaque(false); }}, gbc);

        panel.add(mainContent,  BorderLayout.CENTER);
        panel.add(createNavBar("History"), BorderLayout.WEST);
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