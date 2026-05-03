package ui;

import app.App;
import javax.swing.*;
import java.awt.*;
import util.UIFactory;
import util.AppColors;

public class LockScreen extends BaseScreen {
    private JPasswordField pinField;
    private JButton unlockButton;
    private JLabel failedAttemptLabel;
    private JLabel lockOutTimerLabel;

    public LockScreen(App app) {
        super(app);
    }

    public void onUnlockClicked() {
        String enteredPin = new String(pinField.getPassword());

        if (app.getAuthManager().verifyPIN(enteredPin)) {
            navigateToDashboard();
        }

        else if (app.getAuthManager().isLockedOut()) {
            showLockoutMessage((int) app.getAuthManager().getSecondsRemaining());
        }

        else {
            showWrongPinError();
        }
    }

    public void showLockoutMessage(int secondsRemaining) {
        lockOutTimerLabel.setText("Too many attempts. Try again in " + secondsRemaining + "s.");
        lockOutTimerLabel.setVisible(true);
    }

    public void showWrongPinError() {
        failedAttemptLabel.setText("Incorrect PIN. Please try again.");
        failedAttemptLabel.setVisible(true);
    }

    public void navigateToDashboard() {
        app.showDashboardScreen();
    }

    @Override
    protected void initComponents() {
        pinField = new JPasswordField();
        pinField.setBackground(AppColors.FIELD);
        pinField.setForeground(Color.WHITE);
        pinField.setCaretColor(Color.WHITE);
        pinField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        unlockButton = UIFactory.createPrimaryButton("Unlock");
        failedAttemptLabel = UIFactory.createErrorLabel();
        lockOutTimerLabel = UIFactory.createSubLabel("", 12);

        unlockButton.addActionListener(e -> onUnlockClicked());
    }

    @Override
    protected void initLayout() {
        panel = new JPanel(new BorderLayout());
        panel.setBackground(AppColors.BACKGROUND);

        JPanel card = UIFactory.createCard(420, 430);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        card.add(UIFactory.createTitleLabel("Enter PIN to Unlock", 13), gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 20, 10, 20);
        gbc.weightx = 1;
        card.add(pinField, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 20, 10, 20);
        gbc.weightx = 1;
        card.add(unlockButton, gbc);

        gbc.gridy++;
        card.add(failedAttemptLabel, gbc);

        gbc.gridy++;
        card.add(lockOutTimerLabel, gbc);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(AppColors.BACKGROUND);
        centerPanel.add(card, new GridBagConstraints());
        panel.add(centerPanel, BorderLayout.CENTER);
    }
}