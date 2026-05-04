package util;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CategoryPillRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {

        Color bg, fg;
        //to be automated with entered categories
        switch (value.toString()) {
            case "Food"          -> { bg = new Color(58, 90, 30);  fg = new Color(150, 220, 80); }
            case "Transport"     -> { bg = new Color(25, 70, 120); fg = new Color(80, 170, 255); }
            case "Health"        -> { bg = new Color(100, 40, 30); fg = new Color(255, 130, 100); }
            case "Entertainment" -> { bg = new Color(60, 50, 100); fg = new Color(175, 169, 236); }
            default              -> { bg = new Color(58, 58, 58);  fg = Color.WHITE; }
        }

        // rounded pill painted manually
        JPanel pill = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                g2.dispose();
            }
        };
        pill.setOpaque(false);
        pill.setLayout(new BorderLayout());

        JLabel text = new JLabel(value.toString(), SwingConstants.CENTER);
        text.setForeground(fg);
        text.setFont(new Font("SansSerif", Font.PLAIN, 14));
        text.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
        pill.add(text, BorderLayout.CENTER);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(isSelected ? new Color(83, 74, 183) : new Color(42, 42, 42));
        wrapper.add(pill);
        return wrapper;
    }
}
