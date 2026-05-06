package ui;

import app.App;
import util.UIFactory;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract base class for all application screens.
 *
 * <p>Provides the shared {@link JPanel} root, a reference to the {@link App}
 * facade, and the reusable left-side navigation bar. Subclasses implement
 * {@link #initComponents()} to build their widgets and {@link #initLayout()}
 * to arrange them.</p>
 */
public abstract class BaseScreen {

    /** Root panel returned to the {@code JFrame} content pane. */
    protected JPanel panel;

    /** Application facade used to trigger navigation and business logic. */
    protected App app;

    /**
     * Constructs the screen, stores the app reference, and calls
     * {@link #initComponents()} followed by {@link #initLayout()}.
     *
     * @param app the central application controller
     */
    public BaseScreen(App app) {
        this.app = app;
        initComponents();
        initLayout();
    }

    /**
     * Builds and returns the shared vertical navigation bar.
     * The active item is highlighted with a purple indicator strip.
     *
     * @param activeItem display name of the currently active nav item
     *                   (e.g., {@code "DashBoard"}, {@code "History"}, {@code "Settings"})
     * @return the fully constructed nav bar panel
     */
    public JPanel createNavBar(String activeItem) {
        JPanel navBar = new JPanel();
        navBar.setLayout(new BoxLayout(navBar, BoxLayout.Y_AXIS));
        navBar.setBackground(new Color(42, 42, 42));
        navBar.setPreferredSize(new Dimension(180, 60));
        navBar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(58, 58, 58)));

        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new GridLayout(2, 1));
        logoPanel.setBackground(new Color(42, 42, 42));
        logoPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(58, 58, 58)));
        logoPanel.setMinimumSize(new Dimension(180, 60));
        logoPanel.setMaximumSize(new Dimension(180, 60));
        logoPanel.setPreferredSize(new Dimension(180, 60));

        JLabel appNameLabel = UIFactory.createNavTitleLabel("Mizan");
        JLabel cycleNameLabel = UIFactory.createNavSubLabel("May 2026", 15);

        appNameLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 0));
        cycleNameLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 0));

        logoPanel.add(appNameLabel);
        logoPanel.add(cycleNameLabel);

        navBar.add(logoPanel);
        navBar.add(createNavItem("DashBoard", activeItem.equals("DashBoard"), () -> app.showDashboardScreen()));
        navBar.add(createNavItem("History", activeItem.equals("History"), () -> app.showHistoryScreen()));
        navBar.add(createNavItem("Settings", activeItem.equals("Settings"), () -> app.showSettingsScreen()));

        return navBar;
    }

    /**
     * Creates a single navigation item consisting of an optional active
     * indicator strip and a clickable button.
     *
     * @param text    label shown on the nav button
     * @param active  {@code true} if this item represents the current screen
     * @param onClick action to run when the item is clicked
     * @return wrapper panel containing the indicator and button
     */
    protected JPanel createNavItem(String text, boolean active, Runnable onClick) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setMaximumSize(new Dimension(180, 40));
        wrapper.setBackground(active ? new Color(55, 52, 90) : new Color(42, 42, 42));
        wrapper.setOpaque(true);
        if (active) {
            JPanel indicatorStrip = new JPanel();
            indicatorStrip.setBackground(new Color(127, 119, 221));
            indicatorStrip.setPreferredSize(new Dimension(2, 40));
            wrapper.add(indicatorStrip, BorderLayout.WEST);
        }
        JButton btn = createNavButton(text, active);
        btn.addActionListener(e -> onClick.run());
        wrapper.add(btn, BorderLayout.CENTER);
        return wrapper;
    }

    /**
     * Creates a styled navigation button with left-aligned text and no border.
     *
     * @param text   button label
     * @param active {@code true} to apply the active background colour
     * @return the configured {@link JButton}
     */
    protected JButton createNavButton(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(180, 40));
        btn.setPreferredSize(new Dimension(180, 40));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setBackground(active ? new Color(55, 52, 90) : new Color(42, 42, 42));
        return btn;
    }

    /**
     * Initialises all UI components (fields, buttons, labels, etc.).
     * Called before {@link #initLayout()}.
     */
    protected abstract void initComponents();

    /**
     * Arranges the initialised components into the {@link #panel} hierarchy.
     * Called after {@link #initComponents()}.
     */
    protected abstract void initLayout();

    /**
     * Returns the root panel to be set as the {@code JFrame} content pane.
     *
     * @return the screen's root {@link JPanel}
     */
    public JPanel getPanel() {
        return panel;
    }
}
