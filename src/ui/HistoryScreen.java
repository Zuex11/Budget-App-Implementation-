package ui;

import app.App;

import javax.swing.*;
import java.awt.*;


public class HistoryScreen  extends BaseScreen
{
    public HistoryScreen(App app)
    {
        super(app);
    }

    @Override
    protected void initComponents()
    {

    }

    @Override
    protected void initLayout()
    {
        panel = new JPanel(new BorderLayout());
        panel.add(createNavBar("History"), BorderLayout.WEST);
    }
}