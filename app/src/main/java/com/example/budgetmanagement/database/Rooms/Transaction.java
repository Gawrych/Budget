package com.example.budgetmanagement.database.Rooms;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "transactions")
public class Transaction {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transactionId")
    private int transactionId;

    @ColumnInfo(name = "categoryId")
    private int categoryId;
    
    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name="amount")
    private float amount;

    @ColumnInfo(name="addDate")
    private long addDate;

    @ColumnInfo(name="lastModifiedData")
    private long lastModifiedData;

    @ColumnInfo(name="profit")
    private boolean profit;

    public Transaction(int transactionId, int categoryId, @NonNull String title, float amount, long addDate, long lastModifiedData, boolean profit) {
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

    public float getAmount() {
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
