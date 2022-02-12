package com.example.budgetmanagement.database.Rooms.Incoming;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.budgetmanagement.database.Rooms.Transaction.Transaction;

@Entity(tableName = "incoming", foreignKeys = @ForeignKey(entity=Transaction.class, parentColumns="transactionId", childColumns="transactionId"))
public class Incoming {

    @PrimaryKey
    @ColumnInfo(name = "incoming_id")
    private int incomingId;

    @ColumnInfo(name = "transaction_id")
    private int transactionId;

    @ColumnInfo(name = "amount_repeat")
    private int amountRepeat;

    @ColumnInfo(name = "period_of_time")
    private long periodOfTime;

    @ColumnInfo(name = "deadline")
    private long deadline;

    @ColumnInfo(name = "modified_date")
    private long modifiedDate;

    @ColumnInfo(name = "add_date")
    private long addDate;

    public Incoming(int incomingId, int transactionId, int amountRepeat, long periodOfTime, long deadline, long modifiedDate, long addDate) {
        this.incomingId = incomingId;
        this.transactionId = transactionId;
        this.amountRepeat = amountRepeat;
        this.periodOfTime = periodOfTime;
        this.deadline = deadline;
        this.modifiedDate = modifiedDate;
        this.addDate = addDate;
    }

    public int getIncomingId() {
        return incomingId;
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
