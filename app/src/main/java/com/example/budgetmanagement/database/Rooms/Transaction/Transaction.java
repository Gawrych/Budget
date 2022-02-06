package com.example.budgetmanagement.database.Rooms.Transaction;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


import com.example.budgetmanagement.database.Rooms.Category.Category;

@Entity(tableName = "transactions",
        foreignKeys = {@ForeignKey(entity = Category.class,
                parentColumns = "category_id",
                childColumns = "category_id",
                onDelete = ForeignKey.CASCADE)
        })
public class Transaction {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int transactionId;

    @ColumnInfo(name = "category_id")
    private int categoryId;

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name="amount")
    private float amount;

    @ColumnInfo(name="add_date")
    private long add_date;

    @ColumnInfo(name="last_modified_data")
    private long lastModifiedData;

    @ColumnInfo(name="profit")
    private boolean profit;

    public Transaction(int transactionId, int categoryId, @NonNull String title, float amount, long add_date, long lastModifiedData, boolean profit) {
        this.transactionId = transactionId;
        this.categoryId = categoryId;
        this.title = title;
        this.amount = amount;
        this.add_date = add_date;
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

    public long getAdd_date() {
        return add_date;
    }

    public long getLastModifiedData() {
        return lastModifiedData;
    }

    public boolean isProfit() {
        return profit;
    }
}
