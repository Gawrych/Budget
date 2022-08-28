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

    @ColumnInfo(name = "validity")
    private byte validity;

    @ColumnInfo(name = "execute")
    private boolean execute;

    @ColumnInfo(name = "repeatDate") // expireDate is better name, change this
    private long repeatDate;

    @ColumnInfo(name = "modifiedDate")
    private long modifiedDate;

    @ColumnInfo(name = "executeDate")
    private long executedDate;

    @ColumnInfo(name = "addDate")
    private long addDate;

    public Coming(int comingId, int transactionId, byte validity, boolean execute, long repeatDate, long modifiedDate, long addDate, long executedDate) {
        this.comingId = comingId;
        this.transactionId = transactionId;
        this.validity = validity;
        this.execute = execute;
        this.repeatDate = repeatDate;
        this.modifiedDate = modifiedDate;
        this.addDate = addDate;
        this.executedDate = executedDate;
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

    public long getExecutedDate() {
        return executedDate;
    }

    public void setComingId(int comingId) {
        this.comingId = comingId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public void setValidity(byte validity) {
        this.validity = validity;
    }

    public void setExecute(boolean execute) {
        this.execute = execute;
    }

    public void setRepeatDate(long repeatDate) {
        this.repeatDate = repeatDate;
    }

    public void setModifiedDate(long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public void setExecutedDate(long executedDate) {
        this.executedDate = executedDate;
    }

    public void setAddDate(long addDate) {
        this.addDate = addDate;
    }
}
