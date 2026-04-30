package app;

import ui.SetupScreen;

import javax.swing.*;

public class App {
    private JFrame frame;

    App()
    {
        frame = new JFrame("Mizan");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,600);
        frame.setResizable(false);
        frame.setVisible(true);
        showSetupScreen();
    }
    public void showSetupScreen()
    {
        frame.setContentPane(new SetupScreen(this).getPanel());
        frame.revalidate();
    }
}
