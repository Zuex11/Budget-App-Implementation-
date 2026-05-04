package util;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class DefaultDarkRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
        label.setForeground(Color.WHITE);
        label.setBackground(isSelected ? new Color(83, 74, 183) : new Color(42, 42, 42));
        label.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));
        return label;
    }
}