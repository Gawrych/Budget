package com.example.budgetmanagement.database.Rooms;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "coming")
public class Coming {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "comingId")
    private int comingId;

    @ColumnInfo(name = "transactionId")
    private int transactionId;

    @ColumnInfo(name = "amountRepeat")
    private int amountRepeat;

    @ColumnInfo(name = "periodOfTime")
    private long periodOfTime;

    @ColumnInfo(name = "deadline")
    private long deadline;

    @ColumnInfo(name = "modifiedDate")
    private long modifiedDate;

    @ColumnInfo(name = "addDate")
    private long addDate;

    public Coming(int comingId, int transactionId, int amountRepeat, long periodOfTime, long deadline, long modifiedDate, long addDate) {
        this.comingId = comingId;
        this.transactionId = transactionId;
        this.amountRepeat = amountRepeat;
        this.periodOfTime = periodOfTime;
        this.deadline = deadline;
        this.modifiedDate = modifiedDate;
        this.addDate = addDate;
    }

    public int getComingId() {
        return comingId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public int getAmountRepeat() {
        return amountRepeat;
    }

    public long getPeriodOfTime() {
        return periodOfTime;
    }

    public long getDeadline() {
        return deadline;
    }

    public long getModifiedDate() {
        return modifiedDate;
    }

    public long getAddDate() {
        return addDate;
    }
}
