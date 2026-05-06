package domain;

import java.time.LocalDateTime;

/**
 * Represents a single expense transaction logged by the user.
 * Each expense belongs to a {@link BudgetCycle} and a {@link Category}.
 */
public class Expense {

    /** Unique database identifier for this expense. */
    private int id;

    /** Monetary amount of the expense in EGP. */
    private double amount;

    /** ID of the {@link Category} this expense belongs to. */
    private int categoryId;

    /** ID of the {@link BudgetCycle} this expense is recorded under. */
    private int cycleId;

    /** Date and time when this expense was created. */
    private LocalDateTime timestamp;

    /**
     * Constructs a new Expense and stamps it with the current date and time.
     *
     * @param amount     monetary value in EGP (must be &gt; 0)
     * @param categoryId ID of the associated category
     * @param cycleId    ID of the active budget cycle
     */
    public Expense(double amount, int categoryId, int cycleId) {
        this.amount = amount;
        this.categoryId = categoryId;
        this.cycleId = cycleId;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Returns the unique ID of this expense.
     *
     * @return expense ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the monetary amount of this expense.
     *
     * @return amount in EGP
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Returns the category ID associated with this expense.
     *
     * @return category ID
     */
    public int getCategoryId() {
        return categoryId;
    }

    /**
     * Returns the budget cycle ID this expense belongs to.
     *
     * @return cycle ID
     */
    public int getCycleId() {
        return cycleId;
    }

    /**
     * Returns the timestamp when this expense was recorded.
     *
     * @return creation date and time
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Updates the monetary amount of this expense.
     *
     * @param amount new amount in EGP (must be &gt; 0)
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Updates the category associated with this expense.
     *
     * @param categoryId new category ID
     */
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * Sets the database ID for this expense (called after DB insert).
     *
     * @param id generated database ID
     */
    public void setId(int id) {
        this.id = id;
    }
}
