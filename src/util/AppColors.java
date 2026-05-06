package util;

import java.awt.Color;

/**
 * Centralised colour palette for the Mizan dark theme.
 *
 * <p>All UI components should reference these constants instead of
 * hard-coding colour values, ensuring visual consistency and making
 * theme changes a single-file edit.</p>
 */
public class AppColors {

    /** Utility class — do not instantiate. */
    private AppColors() {}

    /** Main window and screen background — near-black. */
    public static final Color BACKGROUND = new Color(30, 30, 30);

    /** Card and panel background — slightly lighter than {@link #BACKGROUND}. */
    public static final Color CARD = new Color(42, 42, 42);

    /** Input field background — used for text fields and spinners. */
    public static final Color FIELD = new Color(58, 58, 58);

    /** Primary text colour — white. */
    public static final Color TEXT_PRIMARY = Color.WHITE;

    /** Secondary text colour — muted grey for labels and placeholders. */
    public static final Color TEXT_SECONDARY = new Color(170, 170, 170);

    /** Error/warning text colour — red used for validation messages. */
    public static final Color ERROR = new Color(220, 80, 80);
}
