package com.example.budgetmanagement.database.TransactionRoom;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import lombok.Getter;


import com.example.budgetmanagement.database.CategoryRoom.Category;

@Getter
@Entity(tableName = "transactions",
        foreignKeys = {@ForeignKey(entity = Category.class,
                parentColumns = "category_id",
                childColumns = "category_id",
                onDelete = ForeignKey.CASCADE)
        })
public class Transaction {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int transaction_id;


    @ColumnInfo(name = "category_id")
    private int category_id;

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name="amount")
    private float amount;

    @ColumnInfo(name="add_date")
    private long add_date;

    @ColumnInfo(name="last_modified_data")
    private long last_modified_data;

    @ColumnInfo(name="profit")
    private boolean profit;

    public Transaction(int transaction_id, int category_id, @NonNull String title, float amount, long add_date, long last_modified_data, boolean profit) {
        this.transaction_id = transaction_id;
        this.category_id = category_id;
        this.title = title;
        this.amount = amount;
        this.add_date = add_date;
        this.last_modified_data = last_modified_data;
        this.profit = profit;
    }
}
