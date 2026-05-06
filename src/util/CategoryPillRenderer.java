package util;

import domain.Category;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Table cell renderer that draws category names as rounded pill badges,
 * with a consistent colour assigned to each category by index.
 *
 * <p>The pill is painted using a custom {@link JPanel#paintComponent} override
 * that draws a rounded rectangle. Text is centred inside with horizontal padding.</p>
 */
public class CategoryPillRenderer extends DefaultTableCellRenderer {

    /** Ordered list of categories, used to assign consistent colours by index. */
    private final List<Category> categories;

    /** Background colours cycled by category index. */
    private static final List<Color> BG_COLORS = Arrays.asList(
        new Color(58, 90, 30),
        new Color(25, 70, 120),
        new Color(100, 40, 30),
        new Color(60, 50, 100),
        new Color(58, 58, 58)
    );

    /** Foreground (text) colours cycled by category index. */
    private static final List<Color> FG_COLORS = Arrays.asList(
        new Color(150, 220, 80),
        new Color(80, 170, 255),
        new Color(255, 130, 100),
        new Color(175, 169, 236),
        Color.WHITE
    );

    /**
     * Constructs a CategoryPillRenderer with the full category list.
     *
     * @param categories all categories in the system, used for colour assignment
     */
    public CategoryPillRenderer(List<Category> categories) {
        this.categories = categories;
    }

    /**
     * Returns a centred pill badge component for the given category name.
     * The pill background and text colour are derived from the category's
     * position in the {@link #categories} list.
     *
     * @param table      the table being rendered
     * @param value      the cell value (expected to be a category name string)
     * @param isSelected {@code true} if the row is currently selected
     * @param hasFocus   {@code true} if the cell has keyboard focus
     * @param row        the row index
     * @param column     the column index
     * @return a wrapper panel containing the rendered pill
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(isSelected ? new Color(83, 74, 183) : new Color(42, 42, 42));

        String categoryName = value.toString();

        // Find the category index for consistent colour assignment
        int index = 0;
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getName().equalsIgnoreCase(categoryName)) {
                index = i;
                break;
            }
        }

        Color bg = BG_COLORS.get(index % BG_COLORS.size());
        Color fg = FG_COLORS.get(index % FG_COLORS.size());

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

        JLabel text = new JLabel(categoryName, SwingConstants.CENTER);
        text.setForeground(fg);
        text.setFont(new Font("SansSerif", Font.PLAIN, 14));
        text.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
        pill.add(text, BorderLayout.CENTER);
        wrapper.add(pill);

        return wrapper;
    }
}
