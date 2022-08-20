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
    private final int transactionId;

    @ColumnInfo(name = "validity")
    private final byte validity;

    @ColumnInfo(name = "execute")
    private final boolean execute;

    @ColumnInfo(name = "repeatDate")
    private final long repeatDate;

    @ColumnInfo(name = "modifiedDate")
    private final long modifiedDate;

    @ColumnInfo(name = "addDate")
    private final long addDate;

    public Coming(int comingId, int transactionId, byte validity, boolean execute, long repeatDate, long modifiedDate, long addDate) {
        this.comingId = comingId;
        this.transactionId = transactionId;
        this.validity = validity;
        this.execute = execute;
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

    public byte getValidity() {
        return validity;
    }

    public boolean isExecute() {
        return execute;
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
