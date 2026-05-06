package domain;

/**
 * Represents an expense category used to classify transactions.
 * Each category has a unique ID, a display name, and an optional icon resource.
 */
public class Category {

    /** Unique database identifier for this category. */
    private int id;

    /** Human-readable name displayed in the UI (e.g., "Food", "Transport"). */
    private String name;

    /** Resource ID for the category icon; 0 if no icon is assigned. */
    private int iconResId;

    /**
     * Constructs a Category with all fields.
     *
     * @param id        unique database ID
     * @param name      display name of the category
     * @param iconResId icon resource ID (0 if none)
     */
    public Category(int id, String name, int iconResId) {
        this.id = id;
        this.name = name;
        this.iconResId = iconResId;
    }

    /**
     * Returns the unique ID of this category.
     *
     * @return category ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the display name of this category.
     *
     * @return category name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the icon resource ID for this category.
     *
     * @return icon resource ID, or 0 if none
     */
    public int getIconResId() {
        return iconResId;
    }

    /**
     * Updates the display name of this category.
     *
     * @param name new category name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the category name, used by Swing combo boxes and list renderers.
     *
     * @return category name
     */
    @Override
    public String toString() {
        return name;
    }
}
