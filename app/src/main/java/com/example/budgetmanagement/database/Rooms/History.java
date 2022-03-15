package com.example.budgetmanagement.database.Rooms;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "history")
public class History {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "historyId")
    private int historyId;

    @ColumnInfo(name = "transactionId")
    private int transactionId;

    @ColumnInfo(name = "addDate")
    private long addDate;

    public History(int historyId, int transactionId, long addDate) {
        this.historyId = historyId;
        this.transactionId = transactionId;
        this.addDate = addDate;
    }

    public int getHistoryId() {
        return historyId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public long getAddDate() {
        return addDate;
    }
}
