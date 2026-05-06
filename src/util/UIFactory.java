package util;

import javax.swing.*;
import java.awt.*;

/**
 * Factory class that creates pre-styled Swing components consistent with
 * the Mizan dark theme.
 *
 * <p>All methods are static. Consumers should call these methods instead of
 * constructing raw Swing components, so that styling is applied uniformly
 * across every screen.</p>
 */
public class UIFactory {

    /** Utility class — do not instantiate. */
    private UIFactory() {}

    /**
     * Creates a dark-background panel suitable for use as a main content area.
     *
     * @param layout the layout manager to apply
     * @return a new {@link JPanel} with {@link AppColors#BACKGROUND} background
     */
    public static JPanel createMainPanel(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(new Color(30, 30, 30));
        return panel;
    }

    /**
     * Creates a slightly lighter sub-panel, used for inner content regions.
     *
     * @param layout the layout manager to apply
     * @return a new {@link JPanel} with {@link AppColors#BACKGROUND} background
     */
    public static JPanel createSubPanel(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(new Color(30, 30, 30));
        return panel;
    }

    /**
     * Creates a bold white title label.
     *
     * @param text     the label text
     * @param fontSize point size of the font
     * @return a styled {@link JLabel}
     */
    public static JLabel createTitleLabel(String text, int fontSize) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, fontSize));
        label.setForeground(AppColors.TEXT_PRIMARY);
        return label;
    }

    /**
     * Creates a plain secondary-colour subtitle label.
     *
     * @param text     the label text
     * @param fontSize point size of the font
     * @return a styled {@link JLabel}
     */
    public static JLabel createSubLabel(String text, int fontSize) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, fontSize));
        label.setForeground(AppColors.TEXT_SECONDARY);
        return label;
    }

    /**
     * Creates the large bold app-name label used in the nav bar logo area.
     *
     * @param text the label text (typically "Mizan")
     * @return a styled {@link JLabel} at 27pt bold
     */
    public static JLabel createNavTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 27));
        label.setForeground(AppColors.TEXT_PRIMARY);
        return label;
    }

    /**
     * Creates a secondary-colour nav sub-label (e.g., current cycle month).
     *
     * @param text     the label text
     * @param fontSize point size of the font
     * @return a styled {@link JLabel}
     */
    public static JLabel createNavSubLabel(String text, int fontSize) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, fontSize));
        label.setForeground(AppColors.TEXT_SECONDARY);
        return label;
    }

    /**
     * Creates a small secondary-colour label used above input fields.
     *
     * @param text the field label text
     * @return a styled {@link JLabel} at 12pt plain
     */
    public static JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        label.setForeground(AppColors.TEXT_SECONDARY);
        return label;
    }

    /**
     * Creates a dark-themed text field with placeholder text behaviour.
     * The placeholder is shown in secondary colour and cleared on focus.
     *
     * @param placeholder text shown when the field is empty and unfocused
     * @return a configured {@link JTextField}
     */
    public static JTextField createTextField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setBackground(AppColors.FIELD);
        field.setForeground(AppColors.TEXT_SECONDARY);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.WHITE);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(AppColors.TEXT_SECONDARY);
                }
            }
        });
        return field;
    }

    /**
     * Creates a primary action button with a white border outline style.
     *
     * @param text button label
     * @return a styled {@link JButton} at 160×40 px
     */
    public static JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(30, 30, 30));
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setBorderPainted(true);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));
        btn.setFont(btn.getFont().deriveFont(Font.BOLD, 14f));
        btn.setPreferredSize(new Dimension(160, 40));
        return btn;
    }

    /**
     * Creates a compact normal button, used for inline actions like "Add" or "Reset".
     *
     * @param text button label
     * @return a styled {@link JButton} at 80×20 px
     */
    public static JButton createNormalButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(30, 30, 30));
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setBorderPainted(true);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));
        btn.setFont(btn.getFont().deriveFont(Font.BOLD, 14f));
        btn.setPreferredSize(new Dimension(80, 20));
        return btn;
    }

    /**
     * Creates a secondary (muted) button with a filled field-colour background
     * and no border, used for cancel or less-prominent actions.
     *
     * @param text button label
     * @return a styled {@link JButton} with full-width preferred width
     */
    public static JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(AppColors.FIELD);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(0, 38));
        return btn;
    }

    /**
     * Creates a dark-themed integer spinner within a given range.
     *
     * @param value initial value
     * @param min   minimum allowed value (inclusive)
     * @param max   maximum allowed value (inclusive)
     * @return a configured {@link JSpinner}
     */
    public static JSpinner createNumberSpinner(int value, int min, int max) {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(value, min, max, 1));
        spinner.setBackground(AppColors.FIELD);
        spinner.setBorder(BorderFactory.createEmptyBorder());
        JFormattedTextField tf = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
        tf.setBackground(AppColors.FIELD);
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tf.setHorizontalAlignment(JTextField.CENTER);
        return spinner;
    }

    /**
     * Creates a fixed-size card panel with a dark background and a subtle border.
     *
     * @param width  preferred width in pixels
     * @param height preferred height in pixels
     * @return a styled {@link JPanel} with {@link GridBagLayout}
     */
    public static JPanel createCard(int width, int height) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(AppColors.CARD);
        card.setBorder(BorderFactory.createLineBorder(new Color(58, 58, 58)));
        card.setPreferredSize(new Dimension(width, height));
        return card;
    }

    /**
     * Creates a hidden error label styled in red, shown only when
     * {@link JLabel#setVisible(boolean)} is called with {@code true}.
     *
     * @return a pre-configured error {@link JLabel}
     */
    public static JLabel createErrorLabel() {
        JLabel label = new JLabel("");
        label.setForeground(AppColors.ERROR);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        label.setVisible(false);
        return label;
    }
}
