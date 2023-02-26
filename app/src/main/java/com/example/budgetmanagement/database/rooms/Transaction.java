package com.example.budgetmanagement.database.rooms;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;

@Entity(tableName = "transactions")
public class Transaction {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transactionId")
    private final int transactionId;

    @ColumnInfo(name = "categoryId")
    private final int categoryId;

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name="amount")
    private String amount;

    @ColumnInfo(name = "executed")
    private boolean executed;

    @ColumnInfo(name = "executedDate")
    private long executedDate;

    @ColumnInfo(name = "deadline")
    private long deadline;

    @ColumnInfo(name = "deadlineYear")
    private int deadlineYear;

    @ColumnInfo(name="addDate")
    private final long addDate;

    @ColumnInfo(name = "lastEditDate")
    private long lastEditDate;

    public Transaction(int transactionId, int categoryId, @NonNull String title, String amount,
                       long addDate, long lastEditDate, boolean executed, long deadline,
                       int deadlineYear, long executedDate) {
        this.transactionId = transactionId;
        this.categoryId = categoryId;
        this.title = title;
        this.amount = amount;
        this.executed = executed;
        this.deadline = deadline;
        this.deadlineYear = deadlineYear;
        this.lastEditDate = lastEditDate;
        this.addDate = addDate;
        this.executedDate = executedDate;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public String getAmount() {
        return amount;
    }

    public long getAddDate() {
        return addDate;
    }

    public long getLastEditDate() {
        return this.lastEditDate;
    }

    public boolean isExecuted() {
        return executed;
    }

    public long getExecutedDate() {
        return executedDate;
    }

    public long getDeadline() {
        return deadline;
    }

    public int getDeadlineYear() {
        return deadlineYear;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }

    public void setExecutedDate(long executedDate) {
        this.executedDate = executedDate;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public void setDeadlineYear(int deadlineYear) {
        this.deadlineYear = deadlineYear;
    }

    public void setLastEditDate(long lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    public boolean isProfit() {
        return new BigDecimal(this.amount).signum() == 1;
    }
}
