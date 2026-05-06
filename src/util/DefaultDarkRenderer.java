package util;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Default table cell renderer for text columns in the dark-themed expense table.
 *
 * <p>Renders cell text in white with left padding, and applies a purple
 * selection highlight consistent with the rest of the table.</p>
 */
public class DefaultDarkRenderer extends DefaultTableCellRenderer {

    /** Constructs a DefaultDarkRenderer with default settings. */
    public DefaultDarkRenderer() {
        super();
    }

    /**
     * Returns a left-padded white-text label for the given cell value.
     *
     * @param table      the table being rendered
     * @param value      the cell value
     * @param isSelected {@code true} if the row is currently selected
     * @param hasFocus   {@code true} if the cell has keyboard focus
     * @param row        the row index
     * @param column     the column index
     * @return the configured renderer component
     */
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
