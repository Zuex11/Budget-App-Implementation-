package util;

import app.App;
import domain.Category;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CategoryPillRenderer extends DefaultTableCellRenderer {
    private final List<Category> categories;
    public CategoryPillRenderer(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(isSelected ? new Color(83, 74, 183) : new Color(42, 42, 42));

        String categoryName = value.toString();

        // find matching category index for consistent color
        int index = 0;
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getName().equalsIgnoreCase(categoryName)) {
                index = i;
                break;
            }
        }

        List<Color> bgColors = Arrays.asList(
                new Color(58, 90, 30),
                new Color(25, 70, 120),
                new Color(100, 40, 30),
                new Color(60, 50, 100),
                new Color(58, 58, 58)
        );
        List<Color> fgColors = Arrays.asList(
                new Color(150, 220, 80),
                new Color(80, 170, 255),
                new Color(255, 130, 100),
                new Color(175, 169, 236),
                Color.WHITE
        );

        Color bg = bgColors.get(index % bgColors.size());
        Color fg = fgColors.get(index % fgColors.size());

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
    private Color pickBgCategoryColor()
    {
        Color bgColor;
        List<Color> bgColors = Arrays.asList(
                new Color(58, 90, 30),
                new Color(25, 70, 120),
                new Color(100, 40, 30),
                new Color(60, 50, 100),
                new Color(58, 58, 58)
        );

        Random random = new Random();
        bgColor = bgColors.get(random.nextInt(bgColors.size()));
        return bgColor;
    }
    private Color fgColor()
    {
        Color fgColor;
        List<Color> fgColors = Arrays.asList(
                new Color(150, 220, 80),
                new Color(80, 170, 255),
                new Color(255, 130, 100),
                new Color(175, 169, 236)
        );
        Random random = new Random();
        fgColor = fgColors.get(random.nextInt(fgColors.size()));
        return fgColor;
    }
}
