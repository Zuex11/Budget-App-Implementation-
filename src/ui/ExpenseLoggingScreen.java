package ui;

import app.App;
import javax.swing.*;
import java.awt.*;

public class ExpenseLoggingScreen extends BaseScreen
{
    public ExpenseLoggingScreen(App app) {super(app);}

    @Override
    protected void initComponents()
    {

    }

    @Override
    protected void initLayout()
    {
        panel = new JPanel(new BorderLayout());
        panel.add(createNavBar("Log Expense"), BorderLayout.WEST);
    }
}