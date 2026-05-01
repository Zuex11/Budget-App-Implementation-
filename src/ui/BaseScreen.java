package ui;

import app.App;
import javax.swing.*;
import java.awt.*;

public abstract class BaseScreen {
    protected JPanel panel;
    protected App app;

    public BaseScreen(App app) {
        this.app = app;
        initComponents();
        initLayout();
    }

    protected abstract void initComponents();
    protected abstract void initLayout();

    public JPanel getPanel() {
        return panel;
    }
}