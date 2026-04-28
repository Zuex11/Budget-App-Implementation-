package domain;

import java.time.LocalDateTime;

public class Expense {
    private int id;
    private double amount;
    private int categoryId;
    private int cycleId;
    private LocalDateTime timestamp;

    public Expense(double amount, int categoryId, int cycleId) {
        this.amount = amount;
        this.categoryId = categoryId;
        this.cycleId = cycleId;
        this.timestamp = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getCycleId() {
        return cycleId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setId(int id) {
        this.id = id;
    }
}