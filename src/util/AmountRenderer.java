package util;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class AmountRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(Color.WHITE);
        label.setBackground(isSelected ? new Color(83, 74, 183) : new Color(42, 42, 42));
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
    }
}
