package com.example.budgetmanagement.database.rooms;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "transactions")
public class Transaction {

//    Convert to long
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transactionId")
    private int transactionId;

    @ColumnInfo(name = "categoryId")
    private int categoryId;

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name="amount")
    private String amount;

    // TODO: Change addDate name to some more relevant
    @ColumnInfo(name="addDate")
    private long addDate;

    @ColumnInfo(name="lastModifiedData")
    private long lastModifiedData;

    // Better efficiency with profit value than process amount to check if value is negative?
    @ColumnInfo(name="profit")
    private boolean profit;

    public Transaction(int transactionId, int categoryId, @NonNull String title, String amount, long addDate, long lastModifiedData, boolean profit) {
        this.transactionId = transactionId;
        this.categoryId = categoryId;
        this.title = title;
        this.amount = amount;
        this.addDate = addDate;
        this.lastModifiedData = lastModifiedData;
        this.profit = profit;
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

    public long getLastModifiedData() {
        return lastModifiedData;
    }

    public boolean getProfit() {
        return profit;
    }
}
