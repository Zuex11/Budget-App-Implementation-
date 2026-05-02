package ui;

import app.App;

import javax.swing.*;
import java.awt.*;


public class SettingsScreen extends BaseScreen
{

    public SettingsScreen(App app)
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
        panel.add(createNavBar("Settings"), BorderLayout.WEST);
    }
}