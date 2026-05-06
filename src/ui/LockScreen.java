package ui;

import app.App;
import javax.swing.*;
import java.awt.*;
import util.UIFactory;
import util.AppColors;

/**
 * Screen shown at startup when a PIN has been set.
 *
 * <p>The user must enter the correct PIN to reach the dashboard. After
 * {@code AuthManager.MAX_ATTEMPTS} consecutive failures the screen shows a
 * countdown timer and blocks further attempts until the lockout expires.</p>
 */
public class LockScreen extends BaseScreen {

    /** Field where the user types their PIN (masked). */
    private JPasswordField pinField;

    /** Button that submits the entered PIN for verification. */
    private JButton unlockButton;

    /** Label shown when an incorrect PIN is entered. */
    private JLabel failedAttemptLabel;

    /** Label showing the remaining lockout time in seconds. */
    private JLabel lockOutTimerLabel;

    /**
     * Constructs the LockScreen.
     *
     * @param app the central application controller
     */
    public LockScreen(App app) {
        super(app);
    }

    /**
     * Handles the unlock button click.
     * Checks lockout status first, then verifies the entered PIN.
     * Navigates to the dashboard on success, or shows an appropriate error.
     */
    public void onUnlockClicked() {
        String enteredPin = new String(pinField.getPassword());
        if (app.getAuthManager().isLockedOut()) {
            showLockoutMessage((int) app.getAuthManager().getSecondsRemaining());
        } else if (app.getAuthManager().verifyPIN(enteredPin)) {
            navigateToDashboard();
        } else {
            showWrongPinError();
        }
    }

    /**
     * Displays the lockout message with the remaining wait time.
     *
     * @param secondsRemaining seconds until the lockout expires
     */
    public void showLockoutMessage(int secondsRemaining) {
        lockOutTimerLabel.setText("Too many attempts. Try again in " + secondsRemaining + "s.");
        lockOutTimerLabel.setVisible(true);
    }

    /**
     * Displays the incorrect PIN error message.
     */
    public void showWrongPinError() {
        failedAttemptLabel.setText("Incorrect PIN. Please try again.");
        failedAttemptLabel.setVisible(true);
    }

    /**
     * Navigates to the dashboard screen after successful authentication.
     */
    public void navigateToDashboard() {
        app.showDashboardScreen();
    }

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
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
