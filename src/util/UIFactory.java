package util;
import javax.swing.*;
import java.awt.*;

public class UIFactory {

    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        label.setForeground(AppColors.TEXT_PRIMARY);
        return label;
    }

    public static JLabel createSubLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        label.setForeground(AppColors.TEXT_SECONDARY);
        return label;
    }

    public static JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        label.setForeground(AppColors.TEXT_SECONDARY);
        return label;
    }

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

    public static JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(220, 220, 220));
        btn.setForeground(new Color(20, 20, 20));
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(btn.getFont().deriveFont(Font.BOLD, 14f));
        btn.setPreferredSize(new Dimension(0, 38));
        return btn;
    }

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

    public static JPanel createCard(int width, int height) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(AppColors.CARD);
        card.setBorder(BorderFactory.createLineBorder(new Color(58, 58, 58)));
        card.setPreferredSize(new Dimension(width, height));
        return card;
    }

    public static JLabel createErrorLabel() {
        JLabel label = new JLabel("");
        label.setForeground(AppColors.ERROR);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        label.setVisible(false);
        return label;
    }
}