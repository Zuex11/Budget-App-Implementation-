package util;

import javax.swing.*;
import java.awt.*;

/**
 * Custom painted panel that renders a circular arc gauge showing today's
 * spending progress against the daily limit.
 *
 * <p>The arc sweeps clockwise from the top (12 o'clock position). A green
 * foreground arc overlays a grey background circle. The centre displays a
 * "Today's Limit" label, the current value, and the unit.</p>
 */
public class GaugePanel extends JPanel {

    /** Fill fraction of the gauge arc, from 0.0 (empty) to 1.0 (full). */
    private double percentage;

    /** Numeric value displayed in the centre of the gauge (e.g., daily limit). */
    private String value;

    /** Unit label displayed below the value (e.g., "EGP"). */
    private String unit;

    /**
     * Constructs a GaugePanel with an initial state.
     *
     * @param percentage initial fill fraction (0.0–1.0)
     * @param value      initial centre value string
     * @param unit       unit label string (e.g., "EGP")
     */
    public GaugePanel(double percentage, String value, String unit) {
        this.percentage = percentage;
        this.value = value;
        this.unit = unit;
        setPreferredSize(new Dimension(220, 220));
        setBackground(new Color(30, 30, 30));
        setOpaque(true);
    }

    /**
     * Paints the gauge: grey background circle, green progress arc,
     * and centred text labels.
     *
     * @param g the graphics context provided by Swing
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        int radius = 90;
        int strokeWidth = 12;

        // Background circle
        g2.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(58, 58, 58));
        g2.drawArc(cx - radius, cy - radius, radius * 2, radius * 2, 0, 360);

        // Foreground arc (green, clockwise from top)
        g2.setColor(new Color(74, 222, 128));
        int angle = (int) (360 * percentage);
        g2.drawArc(cx - radius, cy - radius, radius * 2, radius * 2, 90, -angle);

        // "Today's Limit" label
        g2.setColor(new Color(170, 170, 170));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 18));
        FontMetrics fm = g2.getFontMetrics();
        String label = "Today's Limit";
        g2.drawString(label, cx - fm.stringWidth(label) / 2, cy - 28);

        // Value (daily limit amount)
        g2.setColor(new Color(74, 222, 128));
        g2.setFont(new Font("SansSerif", Font.BOLD, 30));
        fm = g2.getFontMetrics();
        g2.drawString(value, cx - fm.stringWidth(value) / 2, cy + 8);

        // Unit label
        g2.setColor(new Color(170, 170, 170));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 18));
        fm = g2.getFontMetrics();
        g2.drawString(unit, cx - fm.stringWidth(unit) / 2, cy + 36);
    }

    /**
     * Updates the gauge fill and centre value, then repaints.
     *
     * @param percentage new fill fraction (0.0–1.0)
     * @param value      new centre value string
     */
    public void update(double percentage, String value) {
        this.percentage = percentage;
        this.value = value;
        repaint();
    }
}
