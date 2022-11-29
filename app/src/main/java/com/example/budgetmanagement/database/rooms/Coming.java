package com.example.budgetmanagement.database.rooms;

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

    @ColumnInfo(name = "executed")
    private boolean executed;

    @ColumnInfo(name = "expireDate")
    private long expireDate;

    @ColumnInfo(name = "expireYear")
    private final int expireYear;

    @ColumnInfo(name = "lastEditDate")
    private long lastEditDate;

    @ColumnInfo(name = "executedDate")
    private long executedDate;

    @ColumnInfo(name = "addDate")
    private final long addDate;

    public Coming(int comingId, int transactionId, boolean executed, long expireDate, int expireYear, long lastEditDate, long addDate, long executedDate) {
        this.comingId = comingId;
        this.transactionId = transactionId;
        this.executed = executed;
        this.expireDate = expireDate;
        this.expireYear = expireYear;
        this.lastEditDate = lastEditDate;
        this.addDate = addDate;
        this.executedDate = executedDate;
    }

    public int getComingId() {
        return comingId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public boolean isExecuted() {
        return executed;
    }

    public long getExpireDate() {
        return expireDate;
    }

    public int getExpireYear() {
        return expireYear;
    }

    public long getLastEditDate() {
        return lastEditDate;
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

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }

    public void setExpireDate(long expireDate) {
        this.expireDate = expireDate;
    }

    public void setLastEditDate(long newDate) {
        this.lastEditDate = newDate;
    }

    public void setExecutedDate(long executedDate) {
        this.executedDate = executedDate;
    }
}
