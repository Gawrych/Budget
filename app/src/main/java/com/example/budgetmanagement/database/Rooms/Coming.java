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

    @ColumnInfo(name = "howMuchRepeat")
    private int howMuchRepeat;

    @ColumnInfo(name = "periodOfTimeBetweenRepeat")
    private long periodOfTimeBetweenRepeat;

    @ColumnInfo(name = "repeatDate")
    private long repeatDate;

    @ColumnInfo(name = "modifiedDate")
    private long modifiedDate;

    @ColumnInfo(name = "addDate")
    private long addDate;

    public Coming(int comingId, int transactionId, int howMuchRepeat, long periodOfTimeBetweenRepeat, long repeatDate, long modifiedDate, long addDate) {
        this.comingId = comingId;
        this.transactionId = transactionId;
        this.howMuchRepeat = howMuchRepeat;
        this.periodOfTimeBetweenRepeat = periodOfTimeBetweenRepeat;
        this.repeatDate = repeatDate;
        this.modifiedDate = modifiedDate;
        this.addDate = addDate;
    }

    public int getComingId() {
        return comingId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public int getHowMuchRepeat() {
        return howMuchRepeat;
    }

    public long getPeriodOfTimeBetweenRepeat() {
        return periodOfTimeBetweenRepeat;
    }

    public long getRepeatDate() {
        return repeatDate;
    }

    public long getModifiedDate() {
        return modifiedDate;
    }

    public long getAddDate() {
        return addDate;
    }
}
