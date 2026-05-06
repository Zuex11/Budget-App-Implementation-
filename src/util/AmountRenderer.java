package util;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Table cell renderer for the Amount column in the expense history table.
 *
 * <p>Renders the amount right-aligned in bold white text, with a purple
 * highlight when the row is selected.</p>
 */
public class AmountRenderer extends DefaultTableCellRenderer {

    /** Constructs an AmountRenderer with default settings. */
    public AmountRenderer() {
        super();
    }

    /**
     * Returns a right-aligned bold label for the given amount value.
     *
     * @param table      the table being rendered
     * @param value      the cell value (expected to be a formatted amount string)
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
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(Color.WHITE);
        label.setBackground(isSelected ? new Color(83, 74, 183) : new Color(42, 42, 42));
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
    }
}
