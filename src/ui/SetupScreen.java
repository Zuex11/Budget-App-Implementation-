package ui;

import javax.swing.*;
import app.App;
public class SetupScreen {
private JTextField allowanceField;
private JSpinner startDateField;
private JSpinner endDateField;
private JButton startCycleButton;
private JLabel errorLabel;

private JPanel panel;
private App app;
public SetupScreen(App app)
{
    this.app=app;
    panel=new JPanel();

    allowanceField = new JTextField();
    startDateField = new JSpinner();
    endDateField = new JSpinner();
    startCycleButton = new JButton("Start Cycle");
    errorLabel = new JLabel();
    panel.add(allowanceField);
    panel.add(startDateField);
    panel.add(endDateField);
    panel.add(startCycleButton);
    panel.add(errorLabel);
}
public void onStarCycleClicked()
{

}
public boolean validateInputs()
{
    return true;
}
public void showError(String message)
{

}
public void navigateToDashboard()
{

}
public JPanel getPanel()
{
    return panel;
}
}