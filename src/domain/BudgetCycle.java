package domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Represents a user-defined budget cycle with a fixed allowance and date range.
 * Tracks spending progress and exposes derived metrics such as remaining balance
 * and daily spending limit.
 */
public class BudgetCycle {

    /** Unique database identifier for this cycle. */
    private int id;

    /** Total money allocated for this cycle in EGP. */
    private double totalAllowance;

    /** First day of the budget cycle (inclusive). */
    private LocalDate startDate;

    /** Last day of the budget cycle (inclusive). */
    private LocalDate endDate;

    /** Current lifecycle status of this cycle. */
    private CycleStatus status;

    /** Running total of all expenses logged in this cycle. */
    private double totalSpent;

    /**
     * Creates a new BudgetCycle with status {@link CycleStatus#UNINITIALIZED}.
     *
     * @param totalAllowance total budget for the cycle in EGP
     * @param startDate      first day of the cycle
     * @param endDate        last day of the cycle
     */
    public BudgetCycle(double totalAllowance, LocalDate startDate, LocalDate endDate) {
        this.totalAllowance = totalAllowance;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = CycleStatus.UNINITIALIZED;
    }

    /**
     * Sets the database ID (called after DB insert).
     *
     * @param id generated database ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Updates the lifecycle status of this cycle.
     *
     * @param status new {@link CycleStatus}
     */
    public void setStatus(CycleStatus status) {
        this.status = status;
    }

    /**
     * Updates the cached total spent amount.
     *
     * @param totalSpent cumulative expenses in EGP
     */
    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
    }

    /**
     * Returns the unique cycle ID.
     * @return unique cycle ID
     */
    public int getId() { return id; }

    /**
     * Returns the total allowance in EGP.
     * @return total allowance in EGP
     */
    public double getTotalAllowance() { return totalAllowance; }

    /**
     * Returns the cycle start date.
     * @return cycle start date
     */
    public LocalDate getStartDate() { return startDate; }

    /**
     * Returns the cycle end date.
     * @return cycle end date
     */
    public LocalDate getEndDate() { return endDate; }

    /**
     * Returns the current cycle status.
     * @return current cycle status
     */
    public CycleStatus getStatus() { return status; }

    /**
     * Calculates the number of days remaining until the cycle ends.
     *
     * @return days from today to end date; 0 if already past
     */
    public int getRemainingDays() {
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), endDate);
    }

    /**
     * Calculates the remaining unspent balance.
     *
     * @return {@code totalAllowance - totalSpent} in EGP
     */
    public double getRemainingBalance() {
        return totalAllowance - totalSpent;
    }

    /**
     * Calculates what percentage of the allowance has been spent.
     *
     * @return percentage from 0 to 100 (may exceed 100 if over-budget)
     */
    public double getSpentPercentage() {
        return (totalSpent / totalAllowance) * 100;
    }

    /**
     * Checks whether this cycle is currently active.
     *
     * @return {@code true} if status is {@link CycleStatus#ACTIVE}
     */
    public boolean isActive() {
        return status == CycleStatus.ACTIVE;
    }
}
