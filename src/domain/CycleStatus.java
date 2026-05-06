package domain;

/**
 * Enumerates the possible lifecycle states of a {@link BudgetCycle}.
 *
 * <ul>
 *   <li>{@link #UNINITIALIZED} – cycle created but not yet activated</li>
 *   <li>{@link #ACTIVE}        – cycle is running normally</li>
 *   <li>{@link #NEAR_LIMIT}    – spending has reached 80 % of allowance</li>
 *   <li>{@link #EXHAUSTED}     – spending has reached or exceeded 100 % of allowance</li>
 *   <li>{@link #ENDED}         – cycle was manually reset or its end date passed</li>
 * </ul>
 */
public enum CycleStatus {

    /** Cycle has been created but not yet started. */
    UNINITIALIZED,

    /** Cycle is active and tracking expenses normally. */
    ACTIVE,

    /** User has spent at least 80 % of the total allowance. */
    NEAR_LIMIT,

    /** User has spent 100 % or more of the total allowance. */
    EXHAUSTED,

    /** Cycle has been ended or reset by the user. */
    ENDED
}
