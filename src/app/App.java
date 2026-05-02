package app;

import com.formdev.flatlaf.FlatDarkLaf;
import control.CycleManager;
import ui.*;

import javax.swing.*;
import java.util.concurrent.CyclicBarrier;

public class App {
    private JFrame frame;
    private CycleManager cycleManager;

    App()
    {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        frame = new JFrame("Mizan");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,600);
        frame.setResizable(false);
        frame.setVisible(true);
        //showSetupScreen();
        showDashboardScreen();
    }
    public void showSetupScreen()
    {
        frame.setContentPane(new SetupScreen(this).getPanel());
        frame.revalidate();
    }
    public void showDashboardScreen()
    {
        frame.setContentPane(new DashboardScreen(this).getPanel());
        frame.revalidate();
    }
    public void showExpenseLoggingScreen()
    {
        frame.setContentPane(new ExpenseLoggingScreen(this).getPanel());
        frame.revalidate();
    }
    public void showHistoryScreen()
    {
        frame.setContentPane(new HistoryScreen(this).getPanel());
        frame.revalidate();
    }
    public void showSettingsScreen(){
        frame.setContentPane(new SettingsScreen(this).getPanel());
        frame.revalidate();
    }
    public CycleManager getCycleManager(){return cycleManager;}
}
