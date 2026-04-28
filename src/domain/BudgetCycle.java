package domain;

import java.time.LocalDate;

public class BudgetCycle {
    private int id;
    private double totalAllowance;
    private LocalDate startDate;
    private LocalDate endDate;
    private CycleStatus status;
    private double totalSpent;

    public BudgetCycle(double totalAllowance, LocalDate startDate, LocalDate endDate) {
        this.totalAllowance = totalAllowance;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = CycleStatus.UNINITIALIZED;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(CycleStatus status) {
        this.status = status;
    }

    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
    }

    public int getId() {
        return id;
    }

    public double getTotalAllowance() {
        return totalAllowance;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public CycleStatus getStatus() {
        return status;
    }

    public int getRemainingDays() {
        return (int) java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), endDate);
    }

    public double getRemainingBalance() {
        return totalAllowance - totalSpent;
    }

    public double getSpentPercentage() {
        return (totalSpent / totalAllowance) * 100;
    }

    public boolean isActive() {
        return status == CycleStatus.ACTIVE;
    }
}