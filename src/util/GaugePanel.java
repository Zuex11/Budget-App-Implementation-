package util;
import javax.swing.*;
import java.awt.*;

public class GaugePanel extends JPanel {
    private double percentage; // 0.0 to 1.0
    private String value;
    private String unit;

    public GaugePanel(double percentage, String value, String unit) {
        this.percentage = percentage;
        this.value = value;
        this.unit = unit;
        setPreferredSize(new Dimension(220, 220));
        setBackground(new Color(30, 30, 30));
        setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        int radius = 90;
        int strokeWidth = 12;

        // background arc (gray)
        g2.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(58, 58, 58));
        g2.drawArc(cx - radius, cy - radius, radius * 2, radius * 2, 0, 360);

        // foreground arc (green)
        g2.setColor(new Color(74, 222, 128));
        int angle = (int) (360 * percentage);
        g2.drawArc(cx - radius, cy - radius, radius * 2, radius * 2, 90, -angle);

        // "Today's Limit" label
        g2.setColor(new Color(170, 170, 170));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 18));
        FontMetrics fm = g2.getFontMetrics();
        String label = "Today's Limit";
        g2.drawString(label, cx - fm.stringWidth(label) / 2, cy - 28);

// value
        g2.setColor(new Color(74, 222, 128));
        g2.setFont(new Font("SansSerif", Font.BOLD, 30));
        fm = g2.getFontMetrics();
        g2.drawString(value, cx - fm.stringWidth(value) / 2, cy + 8);

// unit
        g2.setColor(new Color(170, 170, 170));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 18));
        fm = g2.getFontMetrics();
        g2.drawString(unit, cx - fm.stringWidth(unit) / 2, cy + 36);
    }
}