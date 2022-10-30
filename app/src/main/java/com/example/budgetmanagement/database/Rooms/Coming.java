package com.example.budgetmanagement.database.Rooms;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "coming")
public class Coming {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "comingId")
    private final int comingId;

    @ColumnInfo(name = "transactionId")
    private int transactionId;

    @ColumnInfo(name = "execute")
    private boolean execute;

    @ColumnInfo(name = "expireDate")
    private long expireDate;

    @ColumnInfo(name = "deadline")
    private long deadline;

    @ColumnInfo(name = "executeDate")
    private long executedDate;

    @ColumnInfo(name = "addDate")
    private final long addDate;

    public Coming(int comingId, int transactionId, boolean execute, long expireDate, long deadline, long addDate, long executedDate) {
        this.comingId = comingId;
        this.transactionId = transactionId;
        this.execute = execute;
        this.expireDate = expireDate;
        this.deadline = deadline;
        this.addDate = addDate;
        this.executedDate = executedDate;
    }

    public int getComingId() {
        return comingId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public boolean isExecute() {
        return execute;
    }

    public long getExpireDate() {
        return expireDate;
    }

    public long getDeadline() {
        return deadline;
    }

    public long getAddDate() {
        return addDate;
    }

    public long getExecutedDate() {
        return executedDate;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public void setExecute(boolean execute) {
        this.execute = execute;
    }

    public void setExpireDate(long expireDate) {
        this.expireDate = expireDate;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public void setExecutedDate(long executedDate) {
        this.executedDate = executedDate;
    }
}
